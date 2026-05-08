package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.Driver;
import com.cabbooking.entity.DriverStatus;
import com.cabbooking.mbb.module.map.Coordinate;
import com.cabbooking.mbb.module.map.HaversineDistanceCalculator;
import com.cabbooking.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DriverMatchingService {
    private final DriverRepository driverRepository;
    private final HaversineDistanceCalculator distanceCalculator;

    public DriverMatchingService(DriverRepository driverRepository, HaversineDistanceCalculator distanceCalculator) {
        this.driverRepository = driverRepository;
        this.distanceCalculator = distanceCalculator;
    }

    public List<DriverMatchCandidate> rankCandidates(Coordinate pickup, String requestedVehicleType) {
        return rankCandidates(pickup, requestedVehicleType, RideMode.BALANCED);
    }

    public List<DriverMatchCandidate> rankCandidates(Coordinate pickup, String requestedVehicleType, RideMode rideMode) {
        RideMode safeMode = rideMode == null ? RideMode.BALANCED : rideMode;
        return driverRepository.findByStatus(DriverStatus.AVAILABLE).stream()
                .map(driver -> toCandidate(driver, pickup, requestedVehicleType, safeMode))
                .sorted(Comparator.comparingDouble(DriverMatchCandidate::score).reversed())
                .limit(5)
                .toList();
    }

    private DriverMatchCandidate toCandidate(Driver driver, Coordinate pickup, String requestedVehicleType, RideMode rideMode) {
        double distance = distanceCalculator.distanceKm(
                driver.getCurrentLatitude(), driver.getCurrentLongitude(), pickup.latitude(), pickup.longitude());
        int eta = Math.max(1, (int) Math.ceil((distance / 30.0) * 60));
        double proximityScore = Math.max(0, 1.0 - Math.min(distance, 8.0) / 8.0);
        double ratingScore = (driver.getRating() == null ? 5.0 : driver.getRating()) / 5.0;
        double vehicleFit = requestedVehicleType != null
                && requestedVehicleType.equalsIgnoreCase(driver.getVehicleType()) ? 0.12 : 0.0;
        double modeBonus = modeBonus(driver, distance, proximityScore, ratingScore, rideMode);
        double score = proximityScore * 0.62 + ratingScore * 0.36 + vehicleFit + modeBonus;
        String reason = reasonFor(distance, rideMode);
        return new DriverMatchCandidate(
                driver.getId(),
                driver.getUser().getFullName(),
                driver.getVehicleType(),
                round(distance),
                eta,
                round(score),
                reason);
    }

    private double modeBonus(Driver driver, double distance, double proximityScore,
                             double ratingScore, RideMode rideMode) {
        if (rideMode.isFastest()) {
            return proximityScore * 0.22 + (distance < 2.0 ? 0.08 : 0.0);
        }
        if (rideMode.isSafetyFocused()) {
            return ratingScore * 0.28 + (driver.getTotalRides() != null && driver.getTotalRides() >= 50 ? 0.06 : 0.0);
        }
        if (rideMode.isCostFocused()) {
            return (distance < 3.0 ? 0.08 : 0.0) + (isStandardEfficiencyVehicle(driver) ? 0.06 : 0.0);
        }
        if (rideMode.isEcoFocused()) {
            return isStandardEfficiencyVehicle(driver) ? 0.16 : 0.02;
        }
        return 0.04;
    }

    private boolean isStandardEfficiencyVehicle(Driver driver) {
        String type = driver.getVehicleType() == null ? "" : driver.getVehicleType().trim().toUpperCase();
        return "MINI".equals(type) || "SEDAN".equals(type);
    }

    private String reasonFor(double distance, RideMode rideMode) {
        if (rideMode.isFastest()) {
            return "Fastest mode favors nearest ETA";
        }
        if (rideMode.isSafetyFocused()) {
            return "Safety+ favors high-rated drivers";
        }
        if (rideMode.isCostFocused()) {
            return "Economy favors nearby standard vehicles";
        }
        if (rideMode.isEcoFocused()) {
            return "Eco mode favors efficient vehicles";
        }
        return distance < 1.0 ? "Closest available driver" : "Balanced proximity and rating";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
