package com.schedule.controller.common.exception;

import lombok.Getter;

@Getter
public class CustomSQLException extends BaseException{

    public CustomSQLException(SQLErrorCode sqlErrorCode){
        super(sqlErrorCode.getMessage(), sqlErrorCode.getStatus());
    }
}
