package com.kafka_village_j2.global.exception.enumeration;

import com.kafka_village_j2.global.exception.FailedRequestException;
import lombok.Getter;

@Getter
public enum ExceptionCode {
    SUCCESS("0000", "요청이 정상적으로 수행되었습니다."),
    TIMEOUT("1001", "응답 시간이 초과되었습니다."),
    NOT_EXISTS("1002", "데이터가 존재하지 않습니다."),
    TOKEN_INVALID("1003", "토큰이 유효하지 않습니다."),
    DB_ERROR("1004", "데이터베이스 오류가 발생했습니다."),
    SECRET_ERROR("1005", "데이터 암호화 도중 오류가 발생했습니다."),
    CONVERT_ERROR("1006", "데이터 변환 도중 오류가 발생했습니다."),
    FAILED("9999", "시스템 오류가 발생했습니다. 관리자에게 문의 부탁드립니다.");

    private final String code;
    private final String msg;

    ExceptionCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ExceptionCode getMsgByCode(String code) {
        for (ExceptionCode exceptionCode : ExceptionCode.values()) {
            if (exceptionCode.getCode().equals(code)) {
                return exceptionCode;
            }
        }
        throw new FailedRequestException(ExceptionCode.NOT_EXISTS.getCode(), ExceptionCode.NOT_EXISTS.getMsg(), code);
    }
}