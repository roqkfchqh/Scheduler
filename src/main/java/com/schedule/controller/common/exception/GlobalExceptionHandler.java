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

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(Map.of(
                        "⛔: ", errorCode.getMessage(),
                        "에러코드: ", errorCode.getStatus()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, String> errorMessages = new HashMap<>();
        for(FieldError error : e.getBindingResult().getFieldErrors()){
            errorMessages.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> response = Map.of(
                "⛔: ", errorMessages,
                "에러코드: ", HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
