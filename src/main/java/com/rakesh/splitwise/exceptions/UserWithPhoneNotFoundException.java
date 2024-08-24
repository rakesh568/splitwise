package com.rakesh.splitwise.exceptions;

import org.springframework.http.HttpStatus;

public class UserWithPhoneNotFoundException extends CustomException {
    public UserWithPhoneNotFoundException(String phone) {
        super(String.format("User with phone number %s not found", phone), HttpStatus.NOT_FOUND);
    }
}
