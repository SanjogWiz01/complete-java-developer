package com.cabbooking.service;

import com.cabbooking.entity.Driver;
import com.cabbooking.entity.DriverStatus;
import com.cabbooking.entity.User;
import com.cabbooking.mbb.bridge.event.CabEventBridge;
import com.cabbooking.mbb.module.map.Coordinate;
import com.cabbooking.mbb.module.map.MapNavigationService;
import com.cabbooking.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
    private final MapNavigationService mapNavigationService;
    private final CabEventBridge cabEventBridge;

    public Driver registerDriver(User user, String licenseNumber, String vehicleNumber,
                                String vehicleModel, String vehicleType,
                                Double latitude, Double longitude) {
        Driver driver = Driver.builder()
                .user(user)
                .licenseNumber(licenseNumber)
                .vehicleNumber(vehicleNumber)
                .vehicleModel(vehicleModel)
                .vehicleType(vehicleType)
                .currentLatitude(latitude)
                .currentLongitude(longitude)
                .status(DriverStatus.OFFLINE)
                .build();

        return driverRepository.save(driver);
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
    }

    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE);
    }

    public Driver updateDriverStatus(Long driverId, DriverStatus status) {
        Driver driver = getDriverById(driverId);
        driver.setStatus(status);
        return driverRepository.save(driver);
    }

    public Driver updateDriverLocation(Long driverId, Double latitude, Double longitude) {
        Driver driver = getDriverById(driverId);
        Double previousLatitude = driver.getCurrentLatitude();
        Double previousLongitude = driver.getCurrentLongitude();
        driver.setCurrentLatitude(latitude);
        driver.setCurrentLongitude(longitude);
        Driver saved = driverRepository.save(driver);
        if (mapNavigationService.isMeaningfulMovement(
                Coordinate.of(previousLatitude, previousLongitude, "Previous driver position"),
                Coordinate.of(latitude, longitude, "Updated driver position"))) {
            cabEventBridge.driverLocationChanged(saved, previousLatitude, previousLongitude, "DRIVER_APP");
        }
        return saved;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver findByUserId(Long userId) {
        return driverRepository.findByUserId(userId);
    }
}
