package com.kagimbz.inventoryservice.service;

import com.kagimbz.inventoryservice.dto.InventoryItemRequest;
import com.kagimbz.inventoryservice.dto.InventoryResponse;
import com.kagimbz.inventoryservice.model.InventoryItem;
import com.kagimbz.inventoryservice.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepo inventoryRepo;

    public void addItem(InventoryItemRequest inventoryItemRequest) {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setSkuCode(inventoryItemRequest.getSkuCode());
        inventoryItem.setQuantity(inventoryItemRequest.getQuantity());

        inventoryRepo.save(inventoryItem);
    }

    public List<InventoryResponse> checkItem(List<String> skuCodes) {
        List<String> existingSkuCodes = new ArrayList<>();

        //Existing Sku Codes
        List<InventoryResponse> inventoryResponses = new ArrayList<>(inventoryRepo.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventoryItem -> mapToResponseForExistingSkuCodes(inventoryItem, existingSkuCodes))
                .toList());

        //Non-Existing Sku Codes
        List<InventoryResponse> additionalResponses = skuCodes.stream()
                .filter(skuCode -> !existingSkuCodes.contains(skuCode))
                .map(this::mapToResponseForNonExistingSkuCodes)
                .toList();

        inventoryResponses.addAll(additionalResponses);
        return inventoryResponses;
    }


    private InventoryResponse mapToResponseForExistingSkuCodes(InventoryItem inventoryItem, List<String> existingSkuCodes) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setSkuCode(inventoryItem.getSkuCode());
        inventoryResponse.setInStock(inventoryItem.getQuantity() > 0);
        existingSkuCodes.add(inventoryItem.getSkuCode());

        return inventoryResponse;
    }

    private InventoryResponse mapToResponseForNonExistingSkuCodes(String skuCode) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setSkuCode(skuCode);
        inventoryResponse.setInStock(false);

        return inventoryResponse;
    }
}
