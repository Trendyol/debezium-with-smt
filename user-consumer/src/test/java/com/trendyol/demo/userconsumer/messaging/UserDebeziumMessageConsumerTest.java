package com.trendyol.demo.userconsumer.messaging;


import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.model.user.UserMessage;
import com.trendyol.demo.userconsumer.service.UserCreateHandler;
import com.trendyol.demo.userconsumer.service.UserHandlerDecider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDebeziumMessageConsumerTest {

    @InjectMocks
    UserDebeziumMessageConsumer userDebeziumMessageConsumer;

    @Mock
    UserDebeziumMessageErrorProducer userDebeziumMessageErrorProducer;

    @Mock
    UserHandlerDecider userHandlerDecider;

    @Mock
    UserCreateHandler userCreateHandler;

    @Test
    void it_should_consume_user_message_successfully() {
        //given
        Date date = new Date();
        UserMessage after = new UserMessage();
        after.id = 4074L;
        after.name = "okan";
        after.surname = "yildirim";
        after.email = "okan@email.com";
        after.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setAfter(after);
        userDebeziumMessage.setBefore(after);
        userDebeziumMessage.setOperationCode("r");

        when(userHandlerDecider.decide(userDebeziumMessage)).thenReturn(userCreateHandler);
        //when
        userDebeziumMessageConsumer.consume(userDebeziumMessage, 1, 1L, "topic");
        //then
        verify(userCreateHandler).handle(userDebeziumMessage);
        verifyNoInteractions(userDebeziumMessageErrorProducer);
    }

    @Test
    void it_should_produce_error_message_when_exception_occurred() {
        //given
        Date date = new Date();
        UserMessage after = new UserMessage();
        after.id = 4074L;
        after.name = "okan";
        after.surname = "yildirim";
        after.email = "okan@email.com";
        after.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setAfter(after);
        userDebeziumMessage.setBefore(after);
        userDebeziumMessage.setOperationCode("r");

        when(userHandlerDecider.decide(userDebeziumMessage)).thenThrow(IllegalArgumentException.class);
        //when
        userDebeziumMessageConsumer.consume(userDebeziumMessage, 1, 1L, "topic");
        //then
        verify(userDebeziumMessageErrorProducer).produceError(eq(userDebeziumMessage), any(IllegalArgumentException.class));
        verifyNoInteractions(userCreateHandler);
    }
}