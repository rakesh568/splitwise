package com.rakesh.splitwise.exceptions;

import org.springframework.http.HttpStatus;

public class UserWithPhoneAlreadyPresentException extends CustomException {
    public UserWithPhoneAlreadyPresentException(String phone) {
        super(String.format("User with phone %s is already present", phone), HttpStatus.UNAUTHORIZED);
    }
}
