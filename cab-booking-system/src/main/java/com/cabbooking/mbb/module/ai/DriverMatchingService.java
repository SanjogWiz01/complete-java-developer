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
        return driverRepository.findByStatus(DriverStatus.AVAILABLE).stream()
                .map(driver -> toCandidate(driver, pickup, requestedVehicleType))
                .sorted(Comparator.comparingDouble(DriverMatchCandidate::score).reversed())
                .limit(5)
                .toList();
    }

    private DriverMatchCandidate toCandidate(Driver driver, Coordinate pickup, String requestedVehicleType) {
        double distance = distanceCalculator.distanceKm(
                driver.getCurrentLatitude(), driver.getCurrentLongitude(), pickup.latitude(), pickup.longitude());
        int eta = Math.max(1, (int) Math.ceil((distance / 30.0) * 60));
        double vehicleFit = requestedVehicleType != null
                && requestedVehicleType.equalsIgnoreCase(driver.getVehicleType()) ? 0.12 : 0.0;
        double score = Math.max(0, 1.0 - Math.min(distance, 8.0) / 8.0)
                + ((driver.getRating() == null ? 5.0 : driver.getRating()) / 5.0) * 0.35
                + vehicleFit;
        String reason = distance < 1.0 ? "Closest available driver" : "Balanced proximity and rating";
        return new DriverMatchCandidate(
                driver.getId(),
                driver.getUser().getFullName(),
                driver.getVehicleType(),
                round(distance),
                eta,
                round(score),
                reason);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
