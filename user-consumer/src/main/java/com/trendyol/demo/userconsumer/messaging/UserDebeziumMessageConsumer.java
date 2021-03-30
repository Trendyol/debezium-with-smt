package com.trendyol.demo.userconsumer.messaging;


import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.service.UserDebeziumMessageHandler;
import com.trendyol.demo.userconsumer.service.UserHandlerDecider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserDebeziumMessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(UserDebeziumMessageConsumer.class);

    private final UserDebeziumMessageErrorProducer userDebeziumMessageErrorProducer;
    private final UserHandlerDecider userHandlerDecider;

    public UserDebeziumMessageConsumer(UserDebeziumMessageErrorProducer userDebeziumMessageErrorProducer,
                                       UserHandlerDecider userHandlerDecider) {
        this.userDebeziumMessageErrorProducer = userDebeziumMessageErrorProducer;
        this.userHandlerDecider = userHandlerDecider;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory",
            autoStartup = "${spring.kafka.consumer.enable}")
    public void consume(@Payload UserDebeziumMessage userDebeziumMessage,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("Consumed customer debezium message with topic: {}, and partition: {}, and offset: {}, {}",
                topic, partition, offset, userDebeziumMessage);
        try {
            UserDebeziumMessageHandler userDebeziumMessageHandler = userHandlerDecider.decide(userDebeziumMessage);
            userDebeziumMessageHandler.handle(userDebeziumMessage);

        } catch (Exception e) {
            logger.error("Exception occurred while consuming customer debezium message: {}, exception: {}", userDebeziumMessage, e);
            userDebeziumMessageErrorProducer.produceError(userDebeziumMessage, e);
        }
    }

}
