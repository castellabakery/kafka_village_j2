package com.kafka_village_j2.log.mongodb.repository;

import com.kafka_village_j2.log.mongodb.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongodbLogRepository extends MongoRepository<Log, String> {
    Optional<Log> findByUuid(String uuid);
}
