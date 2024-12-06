package com.kafka_village_j2.kafka.consumer;

import com.kafka_village_j2.kafka.constants.KafkaTopic;
import com.kafka_village_j2.log.mongodb.MongodbDatabaseConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongodbDatabaseConsumer extends DatabaseConsumer {
    private final MongodbDatabaseConsumerService mongodbDatabaseConsumerService;
    public MongodbDatabaseConsumer(MongodbDatabaseConsumerService mongodbDatabaseConsumerService) {
        super(mongodbDatabaseConsumerService);
        this.mongodbDatabaseConsumerService = mongodbDatabaseConsumerService;
    }
    private final String TOPIC = KafkaTopic.MONGODB;

    @KafkaListener(topics = {TOPIC}, groupId = "p2")
    public void consume(ConsumerRecord<String, String> message) {
        super.consumeImpl(message);
    }
}
