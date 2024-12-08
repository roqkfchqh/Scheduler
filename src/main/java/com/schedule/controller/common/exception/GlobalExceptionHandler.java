package com.schedule.controller.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //runtimeException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(Map.of(
                        "⛔: ", errorCode.getMessage(),
                        "에러코드: ", errorCode.getStatus()
                ));
    }

    //sqlException
    @ExceptionHandler(CustomSQLException.class)
    public ResponseEntity<Map<String, Object>> handleCustomSQLException(CustomSQLException e){
        SQLErrorCode sqlErrorCode = e.getSqlErrorCode();
        return ResponseEntity.status(sqlErrorCode.getStatus())
                .body(Map.of(
                        "⛔: ", sqlErrorCode.getMessage(),
                        "에러코드: ", sqlErrorCode.getStatus()
                ));
    }

    //validException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, String> errorMessages = new HashMap<>();
        for(FieldError error : e.getBindingResult().getFieldErrors()){
            errorMessages.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "⛔: ", errorMessages,
                        "에러코드: ", HttpStatus.BAD_REQUEST.value()
                ));
    }
}
