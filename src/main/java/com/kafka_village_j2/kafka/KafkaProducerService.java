package com.kafka_village_j2.kafka;

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
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final MongoTemplate mongoTemplate;

    private final String COLLECTION = "log";

    private final String CREATE = "CREATE";
    private final String UPDATE = "UPDATE";
    private final String DELETE = "DELETE";

    @KafkaListener(topics = CREATE, groupId = "j2")
    public void create(String message) {
        log.info("### message ### - [{}]", message);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map;
        try {
            map = mapper.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new FailedRequestException(ExceptionCode.CONVERT_ERROR, message);
        } catch (Exception e) {
            throw new FailedRequestException(ExceptionCode.FAILED, e.getMessage());
        }

        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION);
        Document document = new Document();
        map.forEach((key, value) -> document.append(key, value));

        InsertOneResult result = collection.insertOne(document);

        log.info("### InsertOneResult ###" + result.getInsertedId());
    }

    @KafkaListener(topics = UPDATE, groupId = "j2")
    public void update(String message) {
        log.info("### message ### - [{}]", message);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, String>> request;
        Map<String, String> filterMap;
        Map<String, String> actionMap;
        try {
            request = mapper.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new FailedRequestException(ExceptionCode.CONVERT_ERROR, message);
        } catch (Exception e) {
            throw new FailedRequestException(ExceptionCode.FAILED, e.getMessage());
        }

        filterMap = request.get("filter");
        actionMap = request.get("action");

        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION);
        Document filter = new Document();
        filterMap.forEach(filter::append);

        Document update = new Document();
        actionMap.forEach(update::append);
        Document action = new Document("$set", update);

        UpdateResult result = collection.updateMany(filter, action);

        log.info("### UpdateResult ###" + result.getModifiedCount());
    }

    @KafkaListener(topics = DELETE, groupId = "j2")
    public void delete(String message) {
        log.info("### message ### - [{}]", message);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, String>> request;
        Map<String, String> filterMap;
        try {
            request = mapper.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new FailedRequestException(ExceptionCode.CONVERT_ERROR, message);
        } catch (Exception e) {
            throw new FailedRequestException(ExceptionCode.FAILED, e.getMessage());
        }

        filterMap = request.get("filter");

        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION);
        Document filter = new Document();
        filterMap.forEach(filter::append);

        DeleteResult result = collection.deleteMany(filter);

        log.info("### UpdateResult ###" + result.getDeletedCount());
    }
}