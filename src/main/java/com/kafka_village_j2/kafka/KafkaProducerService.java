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
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @KafkaListener(topics = {CREATE, UPDATE, DELETE}, groupId = "j2")
    @Transactional
    public void mongodb(ConsumerRecord<String, String> message) {
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

    //    @KafkaListener(topics = CREATE, groupId = "j2")
    public void create(String message) {
        log.info("### message ### - [{}]", message);

        Map<String, Object> map = parseMessage(message);

        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION);
        Document document = new Document();
        map.forEach((key, value) -> document.append(key, value));

        InsertOneResult result = collection.insertOne(document);

        log.info("### InsertOneResult ###" + result.getInsertedId());
    }

    //    @KafkaListener(topics = UPDATE, groupId = "j2")
    public void update(String message) {
        log.info("### message ### - [{}]", message);

        Map<String, Object> request = parseMessage(message);
        Map<String, Object> filterMap;
        Map<String, Object> actionMap;

        filterMap = (Map<String, Object>) request.get("filter");
        actionMap = (Map<String, Object>) request.get("action");
        String key = String.valueOf(request.get("key"));

        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION);
        Document filter = new Document();
        filterMap.forEach(filter::append);

        Document update = new Document();
//        actionMap.forEach((k, value) -> update.append(k, (key.equals("$inc") ? Integer.parseInt(String.valueOf(value)) : value)));
        actionMap.forEach(update::append);
        Document action = new Document(key, update);

        UpdateResult result = collection.updateMany(filter, action);

        log.info("### UpdateResult ###" + result.getModifiedCount());
    }

    //    @KafkaListener(topics = DELETE, groupId = "j2")
    public void delete(String message) {
        log.info("### message ### - [{}]", message);

        Map<String, Map<String, String>> request = parseMessage(message);
        Map<String, String> filterMap;

        filterMap = request.get("filter");

        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION);
        Document filter = new Document();
        filterMap.forEach(filter::append);

        DeleteResult result = collection.deleteMany(filter);

        log.info("### UpdateResult ###" + result.getDeletedCount());
    }
}