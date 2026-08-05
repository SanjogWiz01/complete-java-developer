package server;

import java.util.logging.Logger;

public class SingleResponsibilityService {

    private static final Logger LOGGER = Logger.getLogger(SingleResponsibilityService.class.getName());

    public void processUserRegistration(String username, String email, String password) {
        if (!validateInput(username, email, password)) {
            LOGGER.warning("Validation failed for user: " + username);
            return;
        }
        User user = new User(username, email, password);
        saveToDatabase(user);
        sendWelcomeEmail(user);
        LOGGER.info("User registered successfully: " + username);
    }

    private boolean validateInput(String username, String email, String password) {
        return username != null && !username.isEmpty()
                && email != null && email.contains("@")
                && password != null && password.length() >= 8;
    }

    private void saveToDatabase(User user) {
        System.out.println("Saving user " + user.getUsername() + " to database.");
    }

    private void sendWelcomeEmail(User user) {
        System.out.println("Sending welcome email to " + user.getEmail());
    }

    public static class User {
        private final String username;
        private final String email;
        private final String password;

        public User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }

        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    public static void main(String[] args) {
        SingleResponsibilityService service = new SingleResponsibilityService();
        service.processUserRegistration("john_doe", "john@example.com", "securePass123");
    }
}
