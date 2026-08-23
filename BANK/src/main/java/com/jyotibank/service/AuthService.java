package com.jyotibank.service;

import com.jyotibank.config.AppConfig;
import com.jyotibank.dao.UserDao;
import com.jyotibank.exception.AuthenticationException;
import com.jyotibank.model.User;
import com.jyotibank.model.enums.UserRole;
import com.jyotibank.util.PasswordUtil;

import java.time.LocalDateTime;

public class AuthService {

    private final UserDao userDao;
    private final AuditService auditService;

    public AuthService(UserDao userDao, AuditService auditService) {
        this.userDao = userDao;
        this.auditService = auditService;
    }

    public long registerAdmin(long actorUserId, String username, String password) {
        requireUsernameFree(username);
        User user = new User(username, PasswordUtil.hashPassword(password), UserRole.ADMIN);
        long userId = userDao.create(user);
        auditService.record(actorUserId, "USER_CREATE", "USER", userId, "Admin '" + username + "' registered");
        return userId;
    }

    public long registerCustomerUser(long actorUserId, String username, String password, long customerId) {
        requireUsernameFree(username);
        User user = new User(username, PasswordUtil.hashPassword(password), UserRole.CUSTOMER);
        user.setCustomerId(customerId);
        long userId = userDao.create(user);
        auditService.record(actorUserId, "USER_CREATE", "USER", userId,
                "Customer login '" + username + "' registered for customer " + customerId);
        return userId;
    }

    public User login(String username, String password) {
        int maxAttempts = AppConfig.getInstance().getIntProperty("app.max.login.attempts", 3);
        User user;
        try {
            user = userDao.findByUsername(username)
                    .orElseThrow(() -> new AuthenticationException("Invalid credentials."));
            if (!user.isActive()) {
                throw new AuthenticationException("User account is inactive.");
            }
            if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                throw new AuthenticationException("Invalid credentials.");
            }
        } catch (AuthenticationException e) {
            auditService.record(AuditService.SYSTEM_ACTOR, "LOGIN_FAILED", "USER", null,
                    "Failed login attempt for '" + username + "'");
            throw e;
        }
        userDao.updateLastLogin(user.getUserId(), LocalDateTime.now());
        auditService.record(user.getUserId(), "LOGIN_SUCCESS", "USER", user.getUserId(),
                "User '" + username + "' logged in");
        return user;
    }

    public void logout(User user) {
        if (user != null) {
            auditService.record(user.getUserId(), "LOGOUT", "USER", user.getUserId(),
                    "User '" + user.getUsername() + "' logged out");
        }
    }

    public int getMaxLoginAttempts() {
        return AppConfig.getInstance().getIntProperty("app.max.login.attempts", 3);
    }

    private void requireUsernameFree(String username) {
        if (username == null || username.isBlank()) {
            throw new AuthenticationException("Username must not be empty.");
        }
        if (userDao.existsByUsername(username)) {
            throw new AuthenticationException("Username is already taken.");
        }
    }
}
