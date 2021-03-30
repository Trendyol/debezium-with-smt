package com.trendyol.demo.userconsumer.service;


import com.trendyol.demo.userconsumer.domain.User;
import com.trendyol.demo.userconsumer.model.DebeziumOperationType;
import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.model.user.UserMessage;
import com.trendyol.demo.userconsumer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCreateHandlerTest {

    @InjectMocks
    UserCreateHandler userCreateHandler;

    @Mock
    UserRepository userRepository;

    @Test
    void it_should_insert_user() {
        //given
        Date date = new Date();
        UserMessage after = new UserMessage();
        after.id = 4000L;
        after.name = "betul";
        after.surname = "cetinkaya";
        after.email = "betul@cetinkaya.com";
        after.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setAfter(after);
        userDebeziumMessage.setOperationCode("c");
        //when
        userCreateHandler.handle(userDebeziumMessage);
        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isEqualTo(after.id);
        assertThat(capturedUser.getName()).isEqualTo(after.name);
        assertThat(capturedUser.getSurname()).isEqualTo(after.surname);
        assertThat(capturedUser.getEmail()).isEqualTo(after.email);
        assertThat(capturedUser.getBirthday()).isEqualTo(new Date(date.getTime() / 1000));
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_create() {
        //when
        boolean applicable = userCreateHandler.isApplicable(DebeziumOperationType.CREATE);
        //then
        assertThat(applicable).isTrue();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_read() {
        //when
        boolean applicable = userCreateHandler.isApplicable(DebeziumOperationType.READ);
        //then
        assertThat(applicable).isTrue();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_update() {
        //when
        boolean applicable = userCreateHandler.isApplicable(DebeziumOperationType.UPDATE);
        //then
        assertThat(applicable).isFalse();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_delete() {
        //when
        boolean applicable = userCreateHandler.isApplicable(DebeziumOperationType.DELETE);
        //then
        assertThat(applicable).isFalse();
    }
}