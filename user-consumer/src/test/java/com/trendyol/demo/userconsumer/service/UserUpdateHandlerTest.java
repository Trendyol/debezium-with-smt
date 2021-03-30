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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUpdateHandlerTest {


    @InjectMocks
    UserUpdateHandler userUpdateHandler;

    @Mock
    UserRepository userRepository;

    @Mock
    UserCreateHandler userCreateHandler;

    @Test
    void it_should_update_user() {
        //given
        Date date = new Date();
        UserMessage after = new UserMessage();

        UserMessage before = new UserMessage();
        before.id = 4074L;
        before.name = "okan";
        before.surname = "yildirim";
        before.email = "okan@email.com";
        before.birthday = date.getTime();

        after.id = 4074L;
        after.name = "okan";
        after.surname = "yildirim";
        after.email = "okan@email.com";
        after.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setBefore(before);
        userDebeziumMessage.setAfter(after);
        userDebeziumMessage.setOperationCode("u");

        User oldUser = new User();
        oldUser.setId(4074L);
        oldUser.setName(before.name);
        oldUser.setSurname(before.surname);
        oldUser.setEmail(before.email);
        oldUser.setBirthday(new Date(before.birthday / 1000));

        when(userRepository.findById(4074L)).thenReturn(Optional.of(oldUser));
        //when
        userUpdateHandler.handle(userDebeziumMessage);
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
    void it_should_insert_user_when_user_is_not_found() {
        //given
        Date date = new Date();
        UserMessage after = new UserMessage();

        UserMessage before = new UserMessage();
        before.id = 4074L;
        before.name = "okan";
        before.surname = "yildirim";
        before.email = "okan@email.com";
        before.birthday = date.getTime();

        after.id = 4074L;
        after.name = "okan";
        after.surname = "yildirim";
        after.email = "okan@email.com";
        after.birthday = date.getTime();

        UserDebeziumMessage userDebeziumMessage = new UserDebeziumMessage();
        userDebeziumMessage.setBefore(before);
        userDebeziumMessage.setAfter(after);
        userDebeziumMessage.setOperationCode("u");

        when(userRepository.findById(4074L)).thenReturn(Optional.empty());
        //when
        userUpdateHandler.handle(userDebeziumMessage);
        //then
        verify(userCreateHandler).handle(userDebeziumMessage);
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_update() {
        //when
        boolean applicable = userUpdateHandler.isApplicable(DebeziumOperationType.UPDATE);
        //then
        assertThat(applicable).isTrue();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_create() {
        //when
        boolean applicable = userUpdateHandler.isApplicable(DebeziumOperationType.CREATE);
        //then
        assertThat(applicable).isFalse();
    }

    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_read() {
        //when
        boolean applicable = userUpdateHandler.isApplicable(DebeziumOperationType.READ);
        //then
        assertThat(applicable).isFalse();
    }



    @Test
    void it_should_check_applicable_when_debezium_operation_type_is_delete() {
        //when
        boolean applicable = userUpdateHandler.isApplicable(DebeziumOperationType.DELETE);
        //then
        assertThat(applicable).isFalse();
    }
}