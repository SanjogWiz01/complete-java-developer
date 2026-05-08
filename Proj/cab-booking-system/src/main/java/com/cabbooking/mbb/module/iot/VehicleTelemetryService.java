package com.cabbooking.mbb.module.iot;

import com.cabbooking.entity.Driver;
import com.cabbooking.entity.DriverTelemetry;
import com.cabbooking.mbb.bridge.event.CabEventBridge;
import com.cabbooking.mbb.module.map.Coordinate;
import com.cabbooking.mbb.module.map.MapNavigationService;
import com.cabbooking.repository.DriverRepository;
import com.cabbooking.repository.DriverTelemetryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleTelemetryService {
    private final DriverRepository driverRepository;
    private final DriverTelemetryRepository telemetryRepository;
    private final DriverBehaviorMonitor behaviorMonitor;
    private final MapNavigationService mapNavigationService;
    private final CabEventBridge cabEventBridge;

    public VehicleTelemetryService(DriverRepository driverRepository,
                                   DriverTelemetryRepository telemetryRepository,
                                   DriverBehaviorMonitor behaviorMonitor,
                                   MapNavigationService mapNavigationService,
                                   CabEventBridge cabEventBridge) {
        this.driverRepository = driverRepository;
        this.telemetryRepository = telemetryRepository;
        this.behaviorMonitor = behaviorMonitor;
        this.mapNavigationService = mapNavigationService;
        this.cabEventBridge = cabEventBridge;
    }

    public VehicleTelemetryResponse record(VehicleTelemetryRequest request) {
        if (request.driverId() == null) {
            throw new IllegalArgumentException("driverId is required");
        }
        Driver driver = driverRepository.findById(request.driverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        DriverBehaviorMonitor.BehaviorAssessment behavior = behaviorMonitor.assess(request);
        DriverTelemetry telemetry = DriverTelemetry.builder()
                .driver(driver)
                .latitude(resolveCoordinate(request.latitude(), driver.getCurrentLatitude()))
                .longitude(resolveCoordinate(request.longitude(), driver.getCurrentLongitude()))
                .speedKmph(request.speedKmph())
                .fuelPercentage(request.fuelPercentage())
                .engineTemperatureC(request.engineTemperatureC())
                .brakeIntensity(request.brakeIntensity())
                .acceleration(request.acceleration())
                .engineStatus(normalize(request.engineStatus(), "OK"))
                .riskLevel(behavior.riskLevel())
                .behaviorScore(behavior.behaviorScore())
                .offlineBuffered(Boolean.TRUE.equals(request.offlineBuffered()))
                .recordedAt(request.recordedAt() == null ? LocalDateTime.now() : request.recordedAt())
                .build();
        DriverTelemetry saved = telemetryRepository.save(telemetry);

        boolean locationEventPublished = publishLocationIfMeaningful(driver, saved);
        cabEventBridge.telemetryReceived(saved);

        return new VehicleTelemetryResponse(saved.getId(), driver.getId(), saved.getRiskLevel(),
                saved.getBehaviorScore(), locationEventPublished, "Telemetry accepted");
    }

    public List<DriverTelemetry> latestTelemetry() {
        return telemetryRepository.findTop20ByOrderByRecordedAtDesc();
    }

    private boolean publishLocationIfMeaningful(Driver driver, DriverTelemetry telemetry) {
        Coordinate previous = Coordinate.of(driver.getCurrentLatitude(), driver.getCurrentLongitude(), "Previous driver position");
        Coordinate latest = Coordinate.of(telemetry.getLatitude(), telemetry.getLongitude(), "Latest driver position");
        if (!mapNavigationService.isMeaningfulMovement(previous, latest)) {
            return false;
        }
        Double previousLatitude = driver.getCurrentLatitude();
        Double previousLongitude = driver.getCurrentLongitude();
        driver.setCurrentLatitude(telemetry.getLatitude());
        driver.setCurrentLongitude(telemetry.getLongitude());
        driverRepository.save(driver);
        cabEventBridge.driverLocationChanged(driver, previousLatitude, previousLongitude, "IOT_TELEMETRY");
        return true;
    }

    private Double resolveCoordinate(Double reported, Double fallback) {
        return reported == null ? fallback : reported;
    }

    private String normalize(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim().toUpperCase();
    }
}
