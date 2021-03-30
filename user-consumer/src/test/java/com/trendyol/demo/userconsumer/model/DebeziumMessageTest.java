package com.trendyol.demo.userconsumer.model;

import com.trendyol.demo.userconsumer.model.user.UserMessage;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

class DebeziumMessageTest {

    @Test
    void it_should_check_is_created() {
        //given
        DebeziumMessage<UserMessage> debeziumMessage = new DebeziumMessage<>();
        debeziumMessage.setOperationCode("c");
        //when
        boolean created = debeziumMessage.isCreated();
        //then
        AssertionsForClassTypes.assertThat(created).isTrue();
    }

    @Test
    void it_should_check_is_read() {
        //given
        DebeziumMessage<UserMessage> debeziumMessage = new DebeziumMessage<>();
        debeziumMessage.setOperationCode("r");
        //when
        boolean read = debeziumMessage.isRead();
        //then
        AssertionsForClassTypes.assertThat(read).isTrue();
    }

    @Test
    void it_should_check_is_updated() {
        //given
        DebeziumMessage<UserMessage> debeziumMessage = new DebeziumMessage<>();
        debeziumMessage.setOperationCode("u");
        //when
        boolean updated = debeziumMessage.isUpdated();
        //then
        AssertionsForClassTypes.assertThat(updated).isTrue();
    }

    @Test
    void it_should_check_is_deleted() {
        //given
        DebeziumMessage<UserMessage> debeziumMessage = new DebeziumMessage<>();
        debeziumMessage.setOperationCode("d");
        //when
        boolean deleted = debeziumMessage.isDeleted();
        //then
        AssertionsForClassTypes.assertThat(deleted).isTrue();
    }
}