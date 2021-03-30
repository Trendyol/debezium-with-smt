package com.trendyol.demo.userconsumer.domain.builder;


import com.trendyol.demo.userconsumer.domain.User;

import java.util.Date;

public final class UserBuilder {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Date birthday;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder surname(String surname) {
        this.surname = surname;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder birthDay(Date birthDay) {
        this.birthday = birthDay;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setBirthday(birthday);
        return user;
    }
}
