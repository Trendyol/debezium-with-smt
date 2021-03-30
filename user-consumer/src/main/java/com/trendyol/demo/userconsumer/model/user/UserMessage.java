package com.trendyol.demo.userconsumer.model.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.trendyol.demo.userconsumer.domain.User;
import com.trendyol.demo.userconsumer.domain.builder.UserBuilder;

import java.util.Date;
import java.util.Objects;

@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserMessage {

    public Long id;
    public String name;
    public String surname;
    public String email;
    public Long birthday;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerMessage{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", birthDay=").append(birthday);
        sb.append('}');
        return sb.toString();
    }

    public User toUser() {
        int debeziumDateDivider = 1000;
        return UserBuilder.anUser()
                .id(id)
                .name(name)
                .surname(surname)
                .email(email)
                .birthDay(Objects.nonNull(birthday) ? new Date(birthday / debeziumDateDivider) : null)
                .build();
    }
}
