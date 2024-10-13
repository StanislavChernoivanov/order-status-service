package com.example.order_status_service.config;

import com.example.order_status_service.model.OrderEvent;
import com.example.order_status_service.model.OrderStatusEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${app.kafka.kafkaEventGroupId}")
    private String kafkaEventGroupId;

    @Bean
    public ProducerFactory<String, OrderStatusEvent> orderStatusEventProducerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, OrderStatusEvent> kafkaTemplate(ProducerFactory<String, OrderStatusEvent> orderStatusEventProducerFactory) {
        return new KafkaTemplate<>(orderStatusEventProducerFactory);
    }

    @Bean
    public ConsumerFactory<String, OrderEvent> orderEventConsumerFactory(ObjectMapper objectMapper) {

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(JsonDeserializer.TYPE_MAPPINGS, "OrderEvent:com.example.order_status_service.model.OrderEvent");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaEventGroupId);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> concurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderEvent> orderEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> concurrentKafkaListenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(orderEventConsumerFactory);

        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
}
