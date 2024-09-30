package com.kagimbz.kafka;

import com.kagimbz.dto.OrderPlacedEvent;
import com.kagimbz.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final NotificationService notificationService;

    @KafkaListener(topics = "shoply_notification", groupId = "shoply_notification_consumers")
    public void subscribe(OrderPlacedEvent orderPlacedEvent) {
        log.info("Received notification for order: {}", orderPlacedEvent.getOrderNumber());
        notificationService.sendEmail(orderPlacedEvent);
    }
}
