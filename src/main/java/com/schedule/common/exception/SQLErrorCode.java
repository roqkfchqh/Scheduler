package com.schedule.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SQLErrorCode {
    DATABASE_CONNECTION_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "DB 서버와의 연결이 끊어졌습니다."),
    DATA_TYPE_ERROR(HttpStatus.BAD_REQUEST,"잘못된 데이터타입입니다."),
    UNKNOWN_DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 오류가 발생했습니다."),
    UNIQUE_KEY_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다.");

    private final HttpStatus status;
    private final String message;
}
