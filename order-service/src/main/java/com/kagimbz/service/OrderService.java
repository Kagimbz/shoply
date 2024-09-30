package com.kagimbz.service;

import com.kagimbz.dto.InventoryResponse;
import com.kagimbz.dto.OrderLineItemRequest;
import com.kagimbz.dto.OrderPlacedEvent;
import com.kagimbz.dto.OrderRequest;
import com.kagimbz.kafka.KafkaProducerService;
import com.kagimbz.model.Order;
import com.kagimbz.model.OrderLineItem;
import com.kagimbz.repository.OrderRepo;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepo orderRepo;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaProducerService kafkaProducerService;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests()
                .stream()
                .map(this::mapOrderFromOrderRequest)
                .toList();
        order.setOrderLineItems(orderLineItems);


        //Collect sku codes from orderLineItems for http request to inventory-service
        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItem::getSkuCode)
                .toList();


        //Create custom span id and execute API call to inventory-service inside that span
        Span span = tracer.nextSpan().name("InventoryServiceLookup");

        try (Tracer.SpanInScope ignored = tracer.withSpan(span.start())) {

            //Check if product is in stock (make http request to inventory-service)
            InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/v1/inventory/check-item", uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean productsAreAvailable = Arrays.stream(Objects.requireNonNull(inventoryResponses, "Items not available! Please try again later."))
                    .allMatch(InventoryResponse::isInStock);

            if (productsAreAvailable) {
                orderRepo.save(order);
                kafkaProducerService.publish(new OrderPlacedEvent(order.getOrderNumber()));

            } else {
                Arrays.stream(inventoryResponses).filter(inventoryResponse -> !inventoryResponse.isInStock()).findFirst().ifPresent(inventoryResponse -> {
                    throw new IllegalArgumentException(inventoryResponse.getSkuCode() + " not available! Please try again later.");
                });
            }

            return "Order Placed Successfully";

        } finally {
            span.end();
        }

    }

    private OrderLineItem mapOrderFromOrderRequest (OrderLineItemRequest orderLineItemRequest) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSkuCode(orderLineItemRequest.getSkuCode());
        orderLineItem.setPrice(orderLineItemRequest.getPrice());
        orderLineItem.setQuantity(orderLineItemRequest.getQuantity());

        return orderLineItem;
    }
}
