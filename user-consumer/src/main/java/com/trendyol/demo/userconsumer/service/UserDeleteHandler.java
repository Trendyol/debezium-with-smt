package com.trendyol.demo.userconsumer.service;

import com.trendyol.demo.userconsumer.model.DebeziumOperationType;
import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDeleteHandler implements UserDebeziumMessageHandler {

    private final Logger logger = LoggerFactory.getLogger(UserDeleteHandler.class);

    private final UserRepository userRepository;

    public UserDeleteHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handle(UserDebeziumMessage userDebeziumMessage) {
        Long userId = userDebeziumMessage.getBefore().id;
        userRepository.deleteById(userId);
        logger.info("User is deleted successfully with id {}", userId);
    }

    @Override
    public boolean isApplicable(DebeziumOperationType debeziumOperationType) {
        return DebeziumOperationType.DELETE.equals(debeziumOperationType);
    }
}
