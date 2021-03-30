package com.trendyol.demo.userconsumer.model;

import java.util.Arrays;

public enum DebeziumOperationType {

    READ("r"),
    CREATE("c"),
    UPDATE("u"),
    DELETE("d");

    private final String code;

    DebeziumOperationType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DebeziumOperationType getOperationTypeByCode(String code) {
        return Arrays.stream(values())
                .filter(operation -> operation.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(DebeziumOperationType.class, code));
    }
}
