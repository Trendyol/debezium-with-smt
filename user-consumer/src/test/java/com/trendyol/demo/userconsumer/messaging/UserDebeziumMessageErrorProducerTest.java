package com.trendyol.demo.userconsumer.messaging;

import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.model.user.UserMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDebeziumMessageErrorProducerTest {

    @InjectMocks
    UserDebeziumMessageErrorProducer userDebeziumMessageErrorProducer;

    @Mock
    KafkaTemplate kafkaTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userDebeziumMessageErrorProducer, "errorTopic", "topic");
    }

    @Test
    void it_should_produce_user_message_when_both_after_and_before_is_not_null() {
        //given
        Date date = new Date();

        UserMessage after = new UserMessage();
        after.id = 4074L;
        after.name = "okan";
        after.surname = "yildirim";
        after.email = "okan@email.com";
        after.birthday = date.getTime();

        UserMessage before = new UserMessage();
        before.id = 4000L;
        before.name = "okan";
        before.surname = "yildirim";
        before.email = "okan@email.com";
        before.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setAfter(after);
        userDebeziumMessage.setBefore(before);
        userDebeziumMessage.setOperationCode("r");

        //when
        userDebeziumMessageErrorProducer.produce(userDebeziumMessage);
        //then
        verify(kafkaTemplate).send("topic", "4074", userDebeziumMessage);
    }

    @Test
    void it_should_produce_user_message_when_after_is_not_null() {
        //given
        Date date = new Date();

        UserMessage userMessage = new UserMessage();
        userMessage.id = 4000L;
        userMessage.name = "betul";
        userMessage.surname = "cetinkaya";
        userMessage.email = "betul@cetinkaya.com";
        userMessage.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setAfter(userMessage);
        userDebeziumMessage.setOperationCode("r");

        //when
        userDebeziumMessageErrorProducer.produce(userDebeziumMessage);
        //then
        verify(kafkaTemplate).send("topic", "4000", userDebeziumMessage);
    }

    @Test
    void it_should_produce_user_message_when_before_is_not_null() {
        //given
        Date date = new Date();

        UserMessage userMessage = new UserMessage();
        userMessage.id = 4000L;
        userMessage.name = "betul";
        userMessage.surname = "cetinkaya";
        userMessage.email = "betul@cetinkaya.com";
        userMessage.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setBefore(userMessage);
        userDebeziumMessage.setOperationCode("r");

        //when
        userDebeziumMessageErrorProducer.produce(userDebeziumMessage);
        //then
        verify(kafkaTemplate).send("topic", "4000", userDebeziumMessage);
    }

    @Test
    void it_should_produce_user_message_when_both_after_and_before_is_null() {
        //given
        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setAfter(null);
        userDebeziumMessage.setBefore(null);
        userDebeziumMessage.setOperationCode("r");

        //when
        userDebeziumMessageErrorProducer.produce(userDebeziumMessage);
        //then
        verify(kafkaTemplate).send(eq("topic"), anyString(), eq(userDebeziumMessage));
    }
}