package com.kafka_village_j2.kafka.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DatabaseConsumerService {
    public abstract void create(String message);

    public abstract void update(String message);

    public abstract void delete(String message);
}
