package com.trendyol.demo.userconsumer.model.user;

import com.trendyol.demo.userconsumer.domain.User;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UserMessageTest {

    @Test
    public void it_should_convert_to_User() {
        //given
        Date date = new Date();

        UserMessage userMessage = new UserMessage();
        userMessage.id = 4000L;
        userMessage.name = "betul";
        userMessage.surname = "cetinkaya";
        userMessage.email = "betul@cetinkaya.com";
        userMessage.birthday = date.getTime();
        //when

        User user = userMessage.toUser();
        //then

        assertThat(user.getId()).isEqualTo(userMessage.id);
        assertThat(user.getName()).isEqualTo(userMessage.name);
        assertThat(user.getSurname()).isEqualTo(userMessage.surname);
        assertThat(user.getEmail()).isEqualTo(userMessage.email);
        assertThat(user.getBirthday()).isEqualTo(new Date(date.getTime()/1000));
    }
}