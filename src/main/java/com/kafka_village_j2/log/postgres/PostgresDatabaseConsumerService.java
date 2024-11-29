package com.kafka_village_j2.log.postgres;

import com.kafka_village_j2.global.exception.FailedRequestException;
import com.kafka_village_j2.global.exception.enumeration.ExceptionCode;
import com.kafka_village_j2.kafka.common.MessageParser;
import com.kafka_village_j2.kafka.service.DatabaseConsumerService;
import com.kafka_village_j2.log.dto.LogDto;
import com.kafka_village_j2.log.postgres.entity.Log;
import com.kafka_village_j2.log.postgres.repository.PostgresLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresDatabaseConsumerService extends DatabaseConsumerService {
    private final PostgresLogRepository postgresLogRepository;

    @Override
    public void create(String message) {
        LogDto.Create parsed = MessageParser.parseMessage(message, LogDto.Create.class);
        postgresLogRepository.save(Log.builder()
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

        postgresLogRepository.save(log);
    }

    @Override
    public void delete(String message) {
        LogDto.Delete parsed = MessageParser.parseMessage(message, LogDto.Delete.class);

        postgresLogRepository.delete(this.getLog(parsed.getFilter()));
    }

    private Log getLog(LogDto.Filter filter) {
        return postgresLogRepository.findById(filter.getUuid()).orElseThrow(() -> new FailedRequestException(ExceptionCode.NOT_EXISTS, filter.getUuid()));
    }
}
