package com.rakesh.splitwise.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotBelongToGroupException extends CustomException {
    public UserNotBelongToGroupException(UUID userUuid, UUID groupUuid) {
        super(String.format("User %s does not belong to group %s", userUuid, groupUuid), HttpStatus.UNAUTHORIZED);
    }

}
