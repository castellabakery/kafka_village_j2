package com.kafka_village_j2.log.postgres.repository;

import com.kafka_village_j2.log.postgres.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresLogRepository extends JpaRepository<Log, String> {
}
