package com.kafka_village_j2.config.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kafka_village_j2.config.exception.enumeration.ExceptionCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private final static String SUCCESS = ExceptionCode.SUCCESS.getCode();
    private final static String SUCCESS_MESSAGE = ExceptionCode.SUCCESS.getMsg();
    private final static String FAILED = ExceptionCode.FAILED.getCode();

    private final T data;
    private final String code;
    private final String message;

    protected ResponseDto(T data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseDto<T> success() {
        return new ResponseDto<>(null, SUCCESS, SUCCESS_MESSAGE);
    }

    public static <T> ResponseDto<T> success(String message) {
        return new ResponseDto<>(null, SUCCESS, message);
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data, SUCCESS, SUCCESS_MESSAGE);
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(null, code, message);
    }

    public static <T> ResponseDto<T> fail(String code, String message, T data) {
        return new ResponseDto<>(data, code, message);
    }

}