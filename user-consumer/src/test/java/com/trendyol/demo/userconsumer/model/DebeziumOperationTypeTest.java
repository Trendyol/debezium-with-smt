package com.trendyol.demo.userconsumer.model;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DebeziumOperationTypeTest {


    @Test
    void it_should_find_read_type() {
        //when
        DebeziumOperationType debeziumOperationType = DebeziumOperationType.getOperationTypeByCode("r");
        //then
        assertThat(debeziumOperationType).isEqualTo(DebeziumOperationType.READ);
    }

    @Test
    void it_should_find_create_type() {
        //when
        DebeziumOperationType debeziumOperationType = DebeziumOperationType.getOperationTypeByCode("c");
        //then
        assertThat(debeziumOperationType).isEqualTo(DebeziumOperationType.CREATE);
    }

    @Test
    void it_should_find_update_type() {
        //when
        DebeziumOperationType debeziumOperationType = DebeziumOperationType.getOperationTypeByCode("u");
        //then
        assertThat(debeziumOperationType).isEqualTo(DebeziumOperationType.UPDATE);
    }

    @Test
    void it_should_find_delete_type() {
        //when
        DebeziumOperationType debeziumOperationType = DebeziumOperationType.getOperationTypeByCode("d");
        //then
        assertThat(debeziumOperationType).isEqualTo(DebeziumOperationType.DELETE);
    }

    @Test
    void it_should_throw_exception_when_code_is_invalid() {
        //when
        Throwable throwable = catchThrowable(() -> DebeziumOperationType.getOperationTypeByCode("o"));
        //then
        AssertionsForClassTypes.assertThat(throwable).isInstanceOf(EnumConstantNotPresentException.class);
    }
}