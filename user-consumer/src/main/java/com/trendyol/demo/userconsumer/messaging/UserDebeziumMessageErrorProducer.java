package com.trendyol.demo.userconsumer.messaging;

import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class UserDebeziumMessageErrorProducer extends AbstractKafkaMessageProducer<UserDebeziumMessage> {

    @Value("${spring.kafka.producer.error-topic}")
    private String errorTopic;

    protected UserDebeziumMessageErrorProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    public String getPartitionKey(UserDebeziumMessage message) {
        if (Objects.nonNull(message.getAfter()) && Objects.nonNull(message.getAfter().id)) {
            return String.valueOf(message.getAfter().id);
        }

        if (Objects.nonNull(message.getBefore()) && Objects.nonNull(message.getBefore().id)) {
            return String.valueOf(message.getBefore().id);
        }

        return String.valueOf(UUID.randomUUID());
    }

    @Override
    public String getTopic() {
        return errorTopic;
    }
}
