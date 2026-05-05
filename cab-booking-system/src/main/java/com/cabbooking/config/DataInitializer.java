package com.cabbooking.config;

import com.cabbooking.entity.Driver;
import com.cabbooking.entity.DriverStatus;
import com.cabbooking.entity.User;
import com.cabbooking.entity.UserRole;
import com.cabbooking.repository.DriverRepository;
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
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
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

        seedDriver("maya.driver@example.com", "Maya Singh", "9800000001",
                "KA-01-AB-4521", "BA 2 PA 4521", "Toyota Etios", "SEDAN",
                28.2096, 83.9856, DriverStatus.AVAILABLE);
        seedDriver("arjun.driver@example.com", "Arjun Patel", "9800000002",
                "KA-02-CD-8712", "BA 3 PA 8712", "Hyundai Creta", "SUV",
                28.2009, 83.9821, DriverStatus.AVAILABLE);
        seedDriver("nisha.driver@example.com", "Nisha Rana", "9800000003",
                "KA-03-EF-6690", "BA 4 PA 6690", "Honda Amaze", "MINI",
                28.2439, 83.9483, DriverStatus.OFFLINE);
        seedDriver("karma.driver@example.com", "Karma Gurung", "9800000004",
                "KA-04-GH-7714", "BA 5 PA 7714", "Suzuki Dzire", "SEDAN",
                28.2333, 83.9833, DriverStatus.AVAILABLE);
    }

    private void seedDriver(String email, String fullName, String phoneNumber,
                            String licenseNumber, String vehicleNumber,
                            String vehicleModel, String vehicleType,
                            Double latitude, Double longitude, DriverStatus status) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .password(passwordEncoder.encode("driver123"))
                        .fullName(fullName)
                        .phoneNumber(phoneNumber)
                        .role(UserRole.DRIVER)
                        .address("Pokhara")
                        .build()));

        if (driverRepository.findByUserId(user.getId()) != null) {
            return;
        }

        Driver driver = Driver.builder()
                .user(user)
                .licenseNumber(licenseNumber)
                .vehicleNumber(vehicleNumber)
                .vehicleModel(vehicleModel)
                .vehicleType(vehicleType)
                .currentLatitude(latitude)
                .currentLongitude(longitude)
                .status(status)
                .rating(5.0)
                .totalRides(0)
                .build();

        driverRepository.save(driver);
    }
}
