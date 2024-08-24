package com.rakesh.splitwise.exceptions;

import com.rakesh.splitwise.utils.StringUtils;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {
    private final HttpStatus status;

    protected CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public String getError() {
        // Use the class name in snake case as the error code
        return StringUtils.toSnakeCase(this.getClass().getSimpleName().replace("Exception", ""));
    }

    private CustomErrorResponse getCustomResponse() {
        return new CustomErrorResponse(this.getError(), this.getMessage());
    }

}