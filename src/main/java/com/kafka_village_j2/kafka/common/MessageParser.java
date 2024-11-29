package com.kafka_village_j2.kafka.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka_village_j2.global.exception.FailedRequestException;
import com.kafka_village_j2.global.exception.enumeration.ExceptionCode;

import java.util.Map;

public class MessageParser {
    public static Map parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new FailedRequestException(ExceptionCode.CONVERT_ERROR, message);
        } catch (Exception e) {
            throw new FailedRequestException(ExceptionCode.FAILED, e.getMessage());
        }
    }

    public static <T> T parseMessage(String message, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(message, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new FailedRequestException(ExceptionCode.CONVERT_ERROR, message);
        } catch (Exception e) {
            throw new FailedRequestException(ExceptionCode.FAILED, e.getMessage());
        }
    }
}
