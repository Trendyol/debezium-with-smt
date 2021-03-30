package com.trendyol.demo.userconsumer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DebeziumMessage<T> extends KafkaErrorMessage {

    private T before;
    private T after;

    @JsonProperty("op")
    private String operationCode;

    @JsonProperty("ts_ms")
    private Long timestamp;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DebeziumMessage{");
        sb.append("before=").append(before);
        sb.append(", after=").append(after);
        sb.append(", operation='").append(operationCode).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @JsonIgnore
    public DebeziumOperationType getOperationType() {
        return DebeziumOperationType.getOperationTypeByCode(this.operationCode);
    }

    @JsonIgnore
    public boolean isCreated() {
        return DebeziumOperationType.CREATE.equals(getOperationType());
    }

    @JsonIgnore
    public boolean isRead() {
        return DebeziumOperationType.READ.equals(getOperationType());
    }

    @JsonIgnore
    public boolean isUpdated() {
        return DebeziumOperationType.UPDATE.equals(getOperationType());
    }

    @JsonIgnore
    public boolean isDeleted() {
        return DebeziumOperationType.DELETE.equals(getOperationType());
    }

    public T getBefore() {
        return before;
    }

    public void setBefore(T before) {
        this.before = before;
    }

    public T getAfter() {
        return after;
    }

    public void setAfter(T after) {
        this.after = after;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
