package com.cabbooking.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LocationService {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final BigDecimal BASE_FARE = new BigDecimal("50");
    private static final BigDecimal PER_KM_RATE = new BigDecimal("15");
    private static final BigDecimal PER_MINUTE_RATE = new BigDecimal("2");

    public Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public Integer estimateDuration(Double distance) {
        // Average speed: 40 km/h in city
        return (int) Math.ceil((distance / 40.0) * 60);
    }

    public BigDecimal calculateCost(Double distance, Integer durationMinutes) {
        BigDecimal distanceKm = BigDecimal.valueOf(distance);
        BigDecimal duration = BigDecimal.valueOf(durationMinutes);

        BigDecimal distanceCost = distanceKm.multiply(PER_KM_RATE);
        BigDecimal timeCost = duration.multiply(PER_MINUTE_RATE);
        BigDecimal totalCost = BASE_FARE.add(distanceCost).add(timeCost);

        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }
}
