package com.kafka_village_j2.kafka.consumer;

import com.kafka_village_j2.kafka.constants.KafkaTopic;
import com.kafka_village_j2.log.mongodb.MongodbDatabaseConsumerService;
import com.kafka_village_j2.log.postgres.PostgresDatabaseConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostgresDatabaseConsumer extends DatabaseConsumer {
    private final PostgresDatabaseConsumerService postgresDatabaseConsumerService;
    public PostgresDatabaseConsumer(PostgresDatabaseConsumerService postgresDatabaseConsumerService) {
        super(postgresDatabaseConsumerService);
        this.postgresDatabaseConsumerService = postgresDatabaseConsumerService;
    }
    private final String TOPIC = KafkaTopic.POSTGRES;

    @KafkaListener(topics = {TOPIC}, groupId = "p2")
    public void consume(ConsumerRecord<String, String> message) {
        super.consumeImpl(message);
    }
}
