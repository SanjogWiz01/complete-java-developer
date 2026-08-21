package com.jyotibank.dao;

import com.jyotibank.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserDao {
    long create(User user);
    Optional<User> findById(long userId);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    void updateLastLogin(long userId, LocalDateTime lastLogin);
}
