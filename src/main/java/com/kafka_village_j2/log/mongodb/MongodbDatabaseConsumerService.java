package com.kafka_village_j2.log.mongodb;

import com.kafka_village_j2.global.exception.FailedRequestException;
import com.kafka_village_j2.global.exception.enumeration.ExceptionCode;
import com.kafka_village_j2.kafka.common.MessageParser;
import com.kafka_village_j2.kafka.service.DatabaseConsumerService;
import com.kafka_village_j2.log.dto.LogDto;
import com.kafka_village_j2.log.mongodb.entity.Log;
import com.kafka_village_j2.log.mongodb.repository.MongodbLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongodbDatabaseConsumerService extends DatabaseConsumerService {
    private final MongodbLogRepository mongodbLogRepository;

    @Override
    public void create(String message) {
        LogDto.Create parsed = MessageParser.parseMessage(message, LogDto.Create.class);
        mongodbLogRepository.save(Log.builder()
                .uuid(parsed.getUuid())
                .name(parsed.getName())
                .age(parsed.getAge())
                .build());
    }

    @Override
    public void update(String message) {
        LogDto.Update parsed = MessageParser.parseMessage(message, LogDto.Update.class);

        Log log = this.getLog(parsed.getFilter());

        log.setName(Optional.ofNullable(parsed.getAction().getName()).orElse(log.getName()));
        log.setAge(parsed.getAction().getAge() == 0 ? log.getAge() : parsed.getAction().getAge());

        mongodbLogRepository.save(log);
    }

    @Override
    public void delete(String message) {
        LogDto.Delete parsed = MessageParser.parseMessage(message, LogDto.Delete.class);

        mongodbLogRepository.delete(this.getLog(parsed.getFilter()));
    }

    private Log getLog(LogDto.Filter filter) {
        return mongodbLogRepository.findByUuid(filter.getUuid()).orElseThrow(() -> new FailedRequestException(ExceptionCode.NOT_EXISTS, filter.getUuid()));
    }
}