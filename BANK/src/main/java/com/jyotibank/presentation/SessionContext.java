package com.jyotibank.presentation;

import com.jyotibank.model.User;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * SessionContext — the logged-in user plus idle-timeout tracking.
 *
 * <p>Every menu action calls {@link #touch()}; if the session has been idle
 * longer than the configured {@code app.session.timeout.minutes}, the next
 * check forces a logout.
 */
public class SessionContext {

    private final User user;
    private final LocalDateTime loginAt;
    private LocalDateTime lastActivity;

    public SessionContext(User user) {
        this.user = user;
        this.loginAt = LocalDateTime.now();
        this.lastActivity = loginAt;
    }

    public User getUser() {
        return user;
    }

    public long getUserId() {
        return user.getUserId();
    }

    public boolean isAdmin() {
        return user.isAdmin();
    }

    public void touch() {
        lastActivity = LocalDateTime.now();
    }

    public boolean isExpired(int timeoutMinutes) {
        return Duration.between(lastActivity, LocalDateTime.now()).toMinutes() >= timeoutMinutes;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) — logged in at %s",
                user.getUsername(), user.getRole(), loginAt.toLocalTime().withNano(0));
    }
}
