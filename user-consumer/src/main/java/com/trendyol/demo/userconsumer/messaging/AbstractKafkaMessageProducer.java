package com.trendyol.demo.userconsumer.messaging;

import com.trendyol.demo.userconsumer.model.KafkaErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class AbstractKafkaMessageProducer<T extends KafkaErrorMessage> {

    private final Logger logger = LoggerFactory.getLogger(AbstractKafkaMessageProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    protected AbstractKafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    void produce(T message) {
        String topic = getTopic();
        String partitionKey = getPartitionKey(message);

        kafkaTemplate.send(topic, partitionKey, message);
        logger.info("Message sent, topic: {}, key: {},  message: {},", topic, partitionKey, message);

    }

    void produceError(T message, Exception e) {
        String topic = getTopic();
        String partitionKey = getPartitionKey(message);

        message.errorMessage = e.getMessage();

        kafkaTemplate.send(topic, partitionKey, message);
        logger.info("Message sent, topic: {}, key: {},  message: {},", topic, partitionKey, message);
    }

    public abstract String getPartitionKey(T message);

    public abstract String getTopic();
}
