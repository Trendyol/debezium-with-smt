package com.trendyol.demo.userconsumer.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.DeserializationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaDeserializationErrorHandler implements ContainerAwareErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(KafkaDeserializationErrorHandler.class);

    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> logs, Consumer<?, ?> consumer, MessageListenerContainer messageListenerContainer) {

        doSeeks(logs, consumer);

        if (logs.isEmpty()) {
            logger.warn("Consumer deserialization exception message: {}", thrownException.getMessage());
            return;
        }

        ConsumerRecord<?, ?> log = logs.get(0);
        if (thrownException.getClass().equals(DeserializationException.class)) {
            var exception = (DeserializationException) thrownException;
            var malformedMessage = new String(exception.getData());
            logger.warn("Skipping log with topic: {}, offset: {}, message: {} , exception: {}", log.topic(), log.offset(), malformedMessage, exception.getLocalizedMessage());
        } else {
            logger.warn("Skipping log with topic: {},  offset: {}, partition: {}, exception: {}", log.topic(), log.offset(), log.partition(), thrownException);
        }

    }

    private void doSeeks(List<ConsumerRecord<?, ?>> logs, Consumer<?, ?> consumer) {

        Map<TopicPartition, Long> partitions = new LinkedHashMap<>();
        var first = new AtomicBoolean(true);
        logs.forEach(record -> {
            if (first.get()) {
                partitions.put(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
            } else {
                partitions.computeIfAbsent(new TopicPartition(record.topic(), record.partition()), offset -> record.offset());
            }
            first.set(false);
        });
        partitions.forEach(consumer::seek);

    }
}
