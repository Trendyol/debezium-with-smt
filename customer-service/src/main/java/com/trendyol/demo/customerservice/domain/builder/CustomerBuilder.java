package com.trendyol.demo.customerservice.domain.builder;

import com.trendyol.demo.customerservice.domain.Customer;

import java.util.Date;

public final class CustomerBuilder {
    private String name;
    private String surname;
    private String email;
    private Date birthDay;

    private CustomerBuilder() {
    }

    public static CustomerBuilder aCustomer() {
        return new CustomerBuilder();
    }

    public CustomerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder surname(String surname) {
        this.surname = surname;
        return this;
    }

    public CustomerBuilder email(String email) {
        this.email = email;
        return this;
    }

    public CustomerBuilder birthDay(Date birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public Customer build() {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setBirthday(birthDay);
        return customer;
    }
}
