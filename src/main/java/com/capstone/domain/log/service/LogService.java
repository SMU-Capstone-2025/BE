package com.capstone.domain.log.service;

public interface LogService<T> {
    void saveLogEntityFromPayload(String kafkaTopic, T payload);
}
