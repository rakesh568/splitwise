package com.rakesh.splitwise.repository;

import com.rakesh.splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);
}
