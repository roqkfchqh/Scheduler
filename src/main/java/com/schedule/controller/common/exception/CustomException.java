package com.schedule.controller.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends BaseException {

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }
}
