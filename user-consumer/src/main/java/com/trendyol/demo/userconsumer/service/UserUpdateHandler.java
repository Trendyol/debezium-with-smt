package com.trendyol.demo.userconsumer.service;

import com.trendyol.demo.userconsumer.domain.User;
import com.trendyol.demo.userconsumer.model.DebeziumOperationType;
import com.trendyol.demo.userconsumer.model.user.UserDebeziumMessage;
import com.trendyol.demo.userconsumer.model.user.UserMessage;
import com.trendyol.demo.userconsumer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserUpdateHandler implements UserDebeziumMessageHandler {

    private final Logger logger = LoggerFactory.getLogger(UserUpdateHandler.class);

    private final UserRepository userRepository;
    private final UserCreateHandler userCreateHandler;


    public UserUpdateHandler(UserRepository userRepository, UserCreateHandler createMessageHandler) {
        this.userRepository = userRepository;
        this.userCreateHandler = createMessageHandler;
    }

    @Override
    public void handle(UserDebeziumMessage userDebeziumMessage) {

        Long userId = userDebeziumMessage.getBefore().id;

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            userCreateHandler.handle(userDebeziumMessage);
            return;
        }

        User user = optionalUser.get();
        UserMessage updatedUserMessage = userDebeziumMessage.getAfter();

        user.setName(updatedUserMessage.name);
        user.setSurname(updatedUserMessage.surname);
        user.setEmail(updatedUserMessage.email);
        user.setBirthday(Objects.nonNull(updatedUserMessage.birthday) ? new Date(updatedUserMessage.birthday / 1000) : null);
        user.setName(updatedUserMessage.name);

        userRepository.save(user);
        logger.info("User is updated successfully with id {}", userId);
    }

    @Override
    public boolean isApplicable(DebeziumOperationType debeziumOperationType) {
        return DebeziumOperationType.UPDATE.equals(debeziumOperationType);
    }
}
