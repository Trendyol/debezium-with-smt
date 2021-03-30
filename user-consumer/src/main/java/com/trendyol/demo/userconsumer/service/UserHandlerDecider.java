package com.trendyol.demo.userconsumer.service;


import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserHandlerDecider {

    private final List<UserDebeziumMessageHandler> userDebeziumMessageHandlers;

    public UserHandlerDecider(List<UserDebeziumMessageHandler> userDebeziumMessageHandlers) {
        this.userDebeziumMessageHandlers = userDebeziumMessageHandlers;
    }

    public UserDebeziumMessageHandler decide(UserDebeziumMessage userDebeziumMessage) {
        return userDebeziumMessageHandlers.stream()
                .filter(handler -> handler.isApplicable(userDebeziumMessage.getOperationType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Applicable User Message Handler not found, message: " + userDebeziumMessage));
    }
}
