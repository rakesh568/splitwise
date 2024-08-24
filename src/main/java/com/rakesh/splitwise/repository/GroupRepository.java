package com.rakesh.splitwise.repository;

import com.rakesh.splitwise.model.Group;
import com.rakesh.splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByUuid(UUID uuid);
}
