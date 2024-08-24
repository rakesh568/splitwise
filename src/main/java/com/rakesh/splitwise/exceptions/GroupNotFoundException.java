package com.rakesh.splitwise.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GroupNotFoundException extends CustomException {
    public GroupNotFoundException(UUID groupUuid) {
        super(String.format("Group with uuid %s not found", groupUuid), HttpStatus.NOT_FOUND);
    }
}
