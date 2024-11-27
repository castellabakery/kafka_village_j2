package com.kafka_village_j2.postgres;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka_village_j2.config.exception.FailedRequestException;
import com.kafka_village_j2.config.exception.enumeration.ExceptionCode;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final LogRepository logRepository;

    private final String CREATE = "CREATE_POSTGRES";
    private final String UPDATE = "UPDATE_POSTGRES";
    private final String DELETE = "DELETE_POSTGRES";

    @KafkaListener(topics = {CREATE, UPDATE, DELETE}, groupId = "p2")
    @Transactional
    public void postgres(ConsumerRecord<String, String> message) {
        log.info("### topic / message ### - [{}] / [{}]", message.topic(), message.value());
        String topic = message.topic();

        if (topic.equals(CREATE)) {
            this.create(message.value());
        } else if (topic.equals(UPDATE)) {
            this.update(message.value());
        } else if (topic.equals(DELETE)) {
            this.delete(message.value());
        } else {
            throw new FailedRequestException(ExceptionCode.NOT_EXISTS, topic);
        }
    }

    private Map parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new FailedRequestException(ExceptionCode.CONVERT_ERROR, message);
        } catch (Exception e) {
            throw new FailedRequestException(ExceptionCode.FAILED, e.getMessage());
        }
    }

    public void create(String message) {
        log.info("### message ### - [{}]", message);

        Map<String, Object> map = parseMessage(message);

        Log log = new Log();
        log.setUuid(String.valueOf(map.get("uuid")));
        log.setName(String.valueOf(map.get("name")));
        log.setAge(Integer.parseInt(String.valueOf(map.get("age"))));
        logRepository.save(log);
    }

    public void update(String message) {
        log.info("### message ### - [{}]", message);

        Map<String, Object> request = parseMessage(message);
        Map<String, Object> filterMap;
        Map<String, Object> actionMap;

        filterMap = (Map<String, Object>) request.get("filter");
        actionMap = (Map<String, Object>) request.get("action");

        Log log = logRepository.findById(String.valueOf(filterMap.get("uuid"))).orElseThrow(() -> new FailedRequestException(ExceptionCode.NOT_EXISTS, String.valueOf(filterMap.get("uuid"))));
        log.setName(actionMap.containsKey("name") ? String.valueOf(actionMap.get("name")) : log.getName());
        log.setAge(actionMap.containsKey("age") ? Integer.parseInt(String.valueOf(actionMap.get("age"))) : log.getAge());
        logRepository.save(log);
    }

    public void delete(String message) {
        log.info("### message ### - [{}]", message);

        Map<String, Map<String, String>> request = parseMessage(message);
        Map<String, String> filterMap;

        filterMap = request.get("filter");

        Log log = logRepository.findById(String.valueOf(filterMap.get("uuid"))).orElseThrow(() -> new FailedRequestException(ExceptionCode.NOT_EXISTS, String.valueOf(filterMap.get("uuid"))));
        logRepository.delete(log);
    }
}
