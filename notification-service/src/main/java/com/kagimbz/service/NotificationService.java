package com.kagimbz.service;

import com.kagimbz.dto.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${mail.recipient}")
    private String recipient;

    public void sendEmail(OrderPlacedEvent orderPlacedEvent) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("Order Notification");
        simpleMailMessage.setText("Your order (Order No. " + orderPlacedEvent.getOrderNumber() + ") has been received and will be processed shortly. Thank you for shopping with us!");
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setSentDate(new Date());

        try {
            mailSender.send(simpleMailMessage);
            log.info("Notification Email Sent Successfully to: {}", recipient);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }

    }
}
