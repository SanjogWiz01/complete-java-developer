package com.jyotibank.service;

import com.jyotibank.dao.UserDao;
import com.jyotibank.exception.AuthenticationException;
import com.jyotibank.model.User;
import com.jyotibank.model.enums.UserRole;
import com.jyotibank.util.PasswordUtil;

import java.time.LocalDateTime;

public class AuthService {

    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public long registerAdmin(String username, String password) {
        if (userDao.existsByUsername(username)) {
            throw new AuthenticationException("Username is already taken.");
        }
        User user = new User(username, PasswordUtil.hashPassword(password), UserRole.ADMIN);
        return userDao.create(user);
    }

    public long registerCustomerUser(String username, String password, long customerId) {
        if (userDao.existsByUsername(username)) {
            throw new AuthenticationException("Username is already taken.");
        }
        User user = new User(username, PasswordUtil.hashPassword(password), UserRole.CUSTOMER);
        user.setCustomerId(customerId);
        return userDao.create(user);
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid credentials."));
        if (!user.isActive()) {
            throw new AuthenticationException("User account is inactive.");
        }
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid credentials.");
        }
        userDao.updateLastLogin(user.getUserId(), LocalDateTime.now());
        return user;
    }
}
