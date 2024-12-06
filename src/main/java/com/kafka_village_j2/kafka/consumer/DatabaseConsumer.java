package com.kafka_village_j2.kafka.consumer;

import com.kafka_village_j2.global.exception.FailedRequestException;
import com.kafka_village_j2.global.exception.enumeration.ExceptionCode;
import com.kafka_village_j2.kafka.common.MessageParser;
import com.kafka_village_j2.kafka.enumeration.DdlType;
import com.kafka_village_j2.kafka.service.DatabaseConsumerService;
import com.kafka_village_j2.log.dto.LogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class DatabaseConsumer {
    private DatabaseConsumerService databaseConsumerService;

    public DatabaseConsumer(DatabaseConsumerService databaseConsumerService) {
        this.databaseConsumerService = databaseConsumerService;
    }

    @Transactional
    public void consumeImpl(ConsumerRecord<String, String> message) {
        log.info("### topic / message ### - [{}] / [{}]", message.topic(), message.value());

        LogDto.Message parsedMessage = MessageParser.parseMessage(message.value(), LogDto.Message.class);
        DdlType type = parsedMessage.getType();
        String msg = parsedMessage.getMessage().toString();

        if (type.equals(DdlType.CREATE)) {
            databaseConsumerService.create(msg);
        } else if (type.equals(DdlType.UPDATE)) {
            databaseConsumerService.update(msg);
        } else if (type.equals(DdlType.DELETE)) {
            databaseConsumerService.delete(msg);
        } else {
            throw new FailedRequestException(ExceptionCode.NOT_EXISTS, parsedMessage.toString());
        }
    }
}
