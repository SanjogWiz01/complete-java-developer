package com.cabbooking.config;

import com.cabbooking.entity.User;
import com.cabbooking.entity.UserRole;
import com.cabbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            return;
        }

        User admin = User.builder()
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .fullName("System Administrator")
                .phoneNumber("0000000000")
                .role(UserRole.ADMIN)
                .address("System")
                .build();

        userRepository.save(admin);
    }
}