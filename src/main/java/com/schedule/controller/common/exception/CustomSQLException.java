package com.schedule.controller.common.exception;

import lombok.Getter;

import java.sql.SQLException;

@Getter
public class CustomSQLException extends SQLException{

    private final SQLErrorCode sqlErrorCode;

    public CustomSQLException(SQLErrorCode sqlErrorCode, SQLException e){
        super(sqlErrorCode.getMessage());
        this.sqlErrorCode = sqlErrorCode;
        this.setStackTrace(e.getStackTrace());
    }
}
