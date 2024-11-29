package com.kafka_village_j2.global.exception.handler;

import com.kafka_village_j2.global.domain.dto.ResponseDto;
import com.kafka_village_j2.global.exception.FailedRequestException;
import com.kafka_village_j2.global.exception.enumeration.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> missingException(Exception e) {
        log.error("MISSING EXCEPTION :: [{}], [{}], [{}]", e.getCause(), e.getMessage(), e.getLocalizedMessage());
        return ResponseEntity.ok(ResponseDto.fail(ExceptionCode.FAILED.getCode(), ExceptionCode.FAILED.getMsg()));
    }

    @ExceptionHandler(FailedRequestException.class)
    public ResponseEntity<ResponseDto<?>> apiRequestFailException(FailedRequestException e) {
        log.error("API REQUEST EXCEPTION :: [{}], [{}], [{}]", e.getCode(), e.getReason(), e.getData());
        return ResponseEntity.ok(ResponseDto.fail(e.getCode(), e.getReason(), e.getData()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto<?>> DataIntegrityViolationException(
            DataIntegrityViolationException e) {
        log.error("DATA INTEGRITY VIOLATION EXCEPTION :: [{}]", Objects.requireNonNull(e.getRootCause()).getMessage());
        return ResponseEntity.ok(ResponseDto.fail(ExceptionCode.DB_ERROR.getCode(), ExceptionCode.DB_ERROR.getMsg(), Objects.requireNonNull(e.getRootCause()).getMessage()));
    }
}