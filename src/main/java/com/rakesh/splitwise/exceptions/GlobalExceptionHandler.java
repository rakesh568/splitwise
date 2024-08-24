package com.rakesh.splitwise.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomBaseException(CustomException ex) {
        return new ResponseEntity<>(new CustomErrorResponse(ex.getError(), ex.getMessage()), ex.getStatus());
    }
}
