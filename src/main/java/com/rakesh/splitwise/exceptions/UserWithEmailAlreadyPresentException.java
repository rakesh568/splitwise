package com.rakesh.splitwise.exceptions;

import org.springframework.http.HttpStatus;

public class UserWithEmailAlreadyPresentException extends CustomException {
    public UserWithEmailAlreadyPresentException(String email) {
        super(String.format("User with email %s is already present", email), HttpStatus.UNAUTHORIZED);
    }
}
