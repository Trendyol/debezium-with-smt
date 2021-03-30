package com.trendyol.demo.userconsumer.service;


import com.trendyol.demo.userconsumer.model.DebeziumOperationType;
import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.model.user.UserMessage;
import com.trendyol.demo.userconsumer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDeleteHandlerTest {

    @InjectMocks
    UserDeleteHandler userDeleteHandler;

    @Mock
    UserRepository userRepository;

    @Test
    void it_should_delete_user() {
        //given
        Date date = new Date();
        UserMessage before = new UserMessage();
        before.id = 4000L;
        before.name = "betul";
        before.surname = "cetinkaya";
        before.email = "betul@cetinkaya.com";
        before.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setBefore(before);
        userDebeziumMessage.setOperationCode("d");
        //when
        userDeleteHandler.handle(userDebeziumMessage);
        //then
        verify(userRepository).deleteById(4000L);

    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_delete() {
        //when
        boolean applicable = userDeleteHandler.isApplicable(DebeziumOperationType.DELETE);
        //then
        assertThat(applicable).isTrue();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_create() {
        //when
        boolean applicable = userDeleteHandler.isApplicable(DebeziumOperationType.CREATE);
        //then
        assertThat(applicable).isFalse();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_read() {
        //when
        boolean applicable = userDeleteHandler.isApplicable(DebeziumOperationType.READ);
        //then
        assertThat(applicable).isFalse();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_update() {
        //when
        boolean applicable = userDeleteHandler.isApplicable(DebeziumOperationType.UPDATE);
        //then
        assertThat(applicable).isFalse();
    }
}