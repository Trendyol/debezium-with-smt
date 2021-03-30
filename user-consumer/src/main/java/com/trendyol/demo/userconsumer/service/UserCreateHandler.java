package com.trendyol.demo.userconsumer.service;

import com.trendyol.demo.userconsumer.domain.User;
import com.trendyol.demo.userconsumer.model.DebeziumOperationType;
import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserCreateHandler implements UserDebeziumMessageHandler {

    private final Logger logger = LoggerFactory.getLogger(UserCreateHandler.class);

    private final UserRepository userRepository;

    public UserCreateHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handle(UserDebeziumMessage userDebeziumMessage) {
        User user = userDebeziumMessage.getAfter().toUser();
        userRepository.save(user);
        logger.info("User is inserted successfully with id {}", user.getId());
    }

    @Override
    public boolean isApplicable(DebeziumOperationType debeziumOperationType) {
        return DebeziumOperationType.CREATE.equals(debeziumOperationType) || DebeziumOperationType.READ.equals(debeziumOperationType) ;
    }
}
