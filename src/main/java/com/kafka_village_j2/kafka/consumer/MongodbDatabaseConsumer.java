package com.kafka_village_j2.kafka.consumer;

import com.kafka_village_j2.global.exception.FailedRequestException;
import com.kafka_village_j2.global.exception.enumeration.ExceptionCode;
import com.kafka_village_j2.kafka.constants.KafkaTopic;
import com.kafka_village_j2.log.mongodb.MongodbDatabaseConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongodbDatabaseConsumer {
    private final MongodbDatabaseConsumerService databaseConsumerService;

    private final String CREATE = KafkaTopic.CREATE_MONGODB;
    private final String UPDATE = KafkaTopic.UPDATE_MONGODB;
    private final String DELETE = KafkaTopic.DELETE_MONGODB;

    @KafkaListener(topics = {CREATE, UPDATE, DELETE}, groupId = "p2")
    @Transactional
    public void consume(ConsumerRecord<String, String> message) {
        log.info("### topic / message ### - [{}] / [{}]", message.topic(), message.value());
        String topic = message.topic();

        if (topic.equals(CREATE)) {
            databaseConsumerService.create(message.value());
        } else if (topic.equals(UPDATE)) {
            databaseConsumerService.update(message.value());
        } else if (topic.equals(DELETE)) {
            databaseConsumerService.delete(message.value());
        } else {
            throw new FailedRequestException(ExceptionCode.NOT_EXISTS, topic);
        }
    }

}
