package com.rakesh.splitwise.controller;

import com.rakesh.splitwise.model.User;
import com.rakesh.splitwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<Map<String, UUID>> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        Map<String, UUID> response = new HashMap<>();
        response.put("userId", createdUser.getUuid());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // Endpoint to get a user by UUID
    @GetMapping("/{uuid}")
    public ResponseEntity<Optional<User>> getUserByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(userService.getUserByUuid(uuid));
    }
}