package com.example.order_status_service.listener;

import com.example.order_status_service.model.OrderEvent;
import com.example.order_status_service.model.OrderStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderStatusEventListener {


    @KafkaListener(topics = "${app.kafka.orderStatusEventTopic}"
            , groupId = "${app.kafka.kafkaEventGroupId}"
            , containerFactory = "concurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderStatusEvent orderStatusEvent) {
        log.info("message: {}", orderStatusEvent);
    }
}
