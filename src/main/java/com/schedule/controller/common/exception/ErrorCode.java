package com.schedule.controller.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    PAGING_ERROR(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "비밀번호를 잘못 입력하였습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "404: 없는 페이지입니다."),
    FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500: 서버 관리를 못해서 줴송합니다.."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "502: 잘못된 접근입니다.");

    private final HttpStatus status;
    private final String message;
}
