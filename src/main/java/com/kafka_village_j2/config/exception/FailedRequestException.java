package com.kafka_village_j2.config.exception;

import com.kafka_village_j2.config.exception.enumeration.ExceptionCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class FailedRequestException extends RuntimeException {

    private String code;
    private String reason;
    private String data;

    public FailedRequestException(String code, String reason, Object data) {
        super(reason + "=" + data);
        this.code = code;
        this.reason = reason;
        this.data = String.valueOf(data);
    }

    public FailedRequestException(String code, String reason) {
        super(reason);
        this.code = code;
        this.reason = reason;
    }

    public FailedRequestException(ExceptionCode exceptionCode, Object data) {
        super(exceptionCode.getMsg());
        this.code = exceptionCode.getCode();
        this.reason = exceptionCode.getMsg();
        this.data = String.valueOf(data);
        log.error("### Request Exception ### - [{}]", this.data);
    }
}