package com.trendyol.demo.userconsumer.service;

import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.model.user.UserMessage;
import com.trendyol.demo.userconsumer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest(classes = {
        UserHandlerDecider.class,
        UserCreateHandler.class,
        UserUpdateHandler.class,
        UserDeleteHandler.class
})
class UserHandlerDeciderTest {

    @Autowired
    UserHandlerDecider userHandlerDecider;

    @MockBean
    UserRepository userRepository;


    @Test
    void it_should_return_create_handler_when_message_is_create() {
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
        userDebeziumMessage.setOperationCode("c");
        //when

        UserDebeziumMessageHandler userDebeziumMessageHandler = userHandlerDecider.decide(userDebeziumMessage);
        //then
        assertThat(userDebeziumMessageHandler).isExactlyInstanceOf(UserCreateHandler.class);
    }

    @Test
    void it_should_return_create_handler_when_message_is_read() {
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
        userDebeziumMessage.setOperationCode("r");
        //when

        UserDebeziumMessageHandler userDebeziumMessageHandler = userHandlerDecider.decide(userDebeziumMessage);
        //then
        assertThat(userDebeziumMessageHandler).isExactlyInstanceOf(UserCreateHandler.class);
    }

    @Test
    void it_should_return_update_handler_when_message_is_update() {
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
        userDebeziumMessage.setOperationCode("u");
        //when

        UserDebeziumMessageHandler userDebeziumMessageHandler = userHandlerDecider.decide(userDebeziumMessage);
        //then
        assertThat(userDebeziumMessageHandler).isExactlyInstanceOf(UserUpdateHandler.class);
    }

    @Test
    void it_should_return_delete_handler_when_message_is_delete() {
        //given
        Date date = new Date();
        UserMessage after = new UserMessage();
        after.id = 4074L;
        after.name = "okan";
        after.surname = "yildirim";
        after.email = "okan@email.com";
        after.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setBefore(after);
        userDebeziumMessage.setOperationCode("d");
        //when

        UserDebeziumMessageHandler userDebeziumMessageHandler = userHandlerDecider.decide(userDebeziumMessage);
        //then
        assertThat(userDebeziumMessageHandler).isExactlyInstanceOf(UserDeleteHandler.class);
    }

    @Test
    void it_should_return_throw_exception_when_operation_type_is_invalid() {
        //given
        Date date = new Date();
        UserMessage after = new UserMessage();
        after.id = 4074L;
        after.name = "okan";
        after.surname = "yildirim";
        after.email = "okan@email.com";
        after.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setBefore(after);
        userDebeziumMessage.setOperationCode("o");
        //when

        Throwable throwable = catchThrowable(() -> userHandlerDecider.decide(userDebeziumMessage));
        //then
        assertThat(throwable).isExactlyInstanceOf(EnumConstantNotPresentException.class);
    }
}