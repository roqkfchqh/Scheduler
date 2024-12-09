package com.schedule.common.database;

import com.schedule.common.exception.CustomSQLException;
import com.schedule.common.exception.SQLErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DatabaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseExceptionHandler.class);

    //sql 예외처리 한꺼번에 처리
    public static void sqlExtracted(SQLException e){
        logger.error("SQL Exception 발생: SQLState={}, ErrorCode={}, Message={}",
                e.getSQLState(), e.getErrorCode(), e.getMessage(), e);

        if(e.getSQLState().startsWith("08")){
            throw new CustomSQLException(SQLErrorCode.DATABASE_CONNECTION_ERROR);
        }else if(e.getSQLState().startsWith("22")) {
            throw new CustomSQLException(SQLErrorCode.DATA_TYPE_ERROR);
        }else if(e.getSQLState().contains("23505")){
            throw new CustomSQLException(SQLErrorCode.UNIQUE_KEY_DUPLICATE);
        }else{
            throw new CustomSQLException(SQLErrorCode.UNKNOWN_DATABASE_ERROR);
        }
    }
}
