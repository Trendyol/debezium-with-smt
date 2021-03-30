package com.trendyol.demo.userconsumer.service;


import com.trendyol.demo.userconsumer.model.DebeziumOperationType;
import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;

public interface UserDebeziumMessageHandler {

    void handle(UserDebeziumMessage userDebeziumMessage);

    boolean isApplicable(DebeziumOperationType debeziumOperationType);
}
