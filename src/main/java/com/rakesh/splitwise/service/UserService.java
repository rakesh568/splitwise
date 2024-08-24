package com.rakesh.splitwise.service;

import com.rakesh.splitwise.exceptions.UserWithEmailAlreadyPresentException;
import com.rakesh.splitwise.exceptions.UserWithPhoneAlreadyPresentException;
import com.rakesh.splitwise.model.User;
import com.rakesh.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        Optional<User> existingUserByPhone = userRepository.findByPhone(user.getPhone());
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());

        if (existingUserByPhone.isPresent()) {
            throw new UserWithPhoneAlreadyPresentException(user.getPhone());
        }

        if (existingUserByEmail.isPresent()) {
            throw new UserWithEmailAlreadyPresentException(user.getPhone());
        }

        user.setUuid(UUID.randomUUID());
        return userRepository.save(user);
    }

    public Optional<User> getUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }
}