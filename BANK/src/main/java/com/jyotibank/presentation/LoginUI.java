package com.jyotibank.presentation;

import com.jyotibank.model.User;
import com.jyotibank.service.AuthService;

import java.util.Optional;

/**
 * LoginUI — credential prompt with a configurable attempt cap.
 *
 * <p>After {@code app.max.login.attempts} consecutive failures the user is
 * returned to the welcome screen instead of being allowed to keep guessing.
 */
public class LoginUI {

    private final AuthService authService;

    public LoginUI(AuthService authService) {
        this.authService = authService;
    }

    public Optional<SessionContext> login() {
        int maxAttempts = authService.getMaxLoginAttempts();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            ConsoleIO.header("LOGIN (attempt " + attempt + " of " + maxAttempts + ")");
            String username = ConsoleIO.readNonEmpty("Username: ");
            String password = ConsoleIO.readNonEmpty("Password: ");

            try {
                User user = authService.login(username, password);
                ConsoleIO.info("Welcome, " + user.getUsername() + " [" + user.getRole() + "]");
                return Optional.of(new SessionContext(user));
            } catch (Exception e) {
                ConsoleIO.error(e.getMessage());
            }
        }
        ConsoleIO.error("Too many failed attempts. Returning to main menu.");
        return Optional.empty();
    }
}
