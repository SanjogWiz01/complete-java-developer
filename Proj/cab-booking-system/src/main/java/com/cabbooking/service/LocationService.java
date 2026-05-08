package com.cabbooking.service;

import com.cabbooking.mbb.module.map.HaversineDistanceCalculator;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LocationService {
    private static final BigDecimal MINIMUM_FARE = new BigDecimal("50");
    private static final BigDecimal PER_KM_RATE = new BigDecimal("50");

    private final HaversineDistanceCalculator distanceCalculator;

    public LocationService(HaversineDistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        return distanceCalculator.distanceKm(lat1, lon1, lat2, lon2);
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
