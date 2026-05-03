package com.cabbooking.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LocationService {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final BigDecimal MINIMUM_FARE = new BigDecimal("50");
    private static final BigDecimal PER_KM_RATE = new BigDecimal("50");

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
        BigDecimal totalCost = distanceKm.multiply(PER_KM_RATE);
        if (totalCost.compareTo(MINIMUM_FARE) < 0) {
            totalCost = MINIMUM_FARE;
        }

        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateCost(Double distance, Integer durationMinutes, String vehicleType, String promoCode) {
        BigDecimal subtotal = calculateCost(distance, durationMinutes);
        BigDecimal discount = calculatePromoDiscount(subtotal, promoCode);
        BigDecimal totalCost = subtotal.subtract(discount);

        if (totalCost.compareTo(MINIMUM_FARE) < 0) {
            totalCost = MINIMUM_FARE;
        }

        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculatePromoDiscount(BigDecimal subtotal, String promoCode) {
        if (promoCode == null || promoCode.isBlank()) {
            return BigDecimal.ZERO;
        }

        String normalizedPromo = promoCode.trim().toUpperCase();
        if ("WELCOME10".equals(normalizedPromo)) {
            return subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        }
        if ("CITY25".equals(normalizedPromo)) {
            return new BigDecimal("25.00");
        }
        return BigDecimal.ZERO;
    }
}
