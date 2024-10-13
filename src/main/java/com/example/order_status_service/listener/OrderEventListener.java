package com.example.order_status_service.listener;

import com.example.order_status_service.model.OrderEvent;
import com.example.order_status_service.model.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;
    @Value("${app.kafka.orderStatusEventTopic}")
    private String orderStatusEventTopic;
    @KafkaListener(topics = "${app.kafka.orderEventTopic}"
            , groupId = "${app.kafka.kafkaEventGroupId}"
            , containerFactory = "concurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderEvent orderEvent) {
        log.info("Message - {}", orderEvent);
        kafkaTemplate.send(orderStatusEventTopic, new OrderStatusEvent("CREATED", Instant.now()));

    }
}
