package com.rakesh.splitwise.exceptions;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserWithUuidNotFoundException extends CustomException {
    public UserWithUuidNotFoundException(UUID uuid) {
        super(String.format("User with uuid %s is already present", uuid), HttpStatus.NOT_FOUND);
    }
}
