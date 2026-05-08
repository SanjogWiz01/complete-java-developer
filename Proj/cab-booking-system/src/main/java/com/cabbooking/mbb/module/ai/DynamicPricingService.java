package com.cabbooking.mbb.module.ai;

import com.cabbooking.mbb.module.map.RoutePlan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DynamicPricingService {
    private static final BigDecimal MINIMUM_FARE = new BigDecimal("50");

    public PricingQuote quote(RoutePlan routePlan, String vehicleType, String promoCode,
                              DemandPredictionService.DemandSignal demandSignal) {
        return quote(routePlan, vehicleType, promoCode, demandSignal, RideMode.BALANCED);
    }

    public PricingQuote quote(RoutePlan routePlan, String vehicleType, String promoCode,
                              DemandPredictionService.DemandSignal demandSignal, RideMode rideMode) {
        RideMode safeMode = rideMode == null ? RideMode.BALANCED : rideMode;
        BigDecimal perKm = rateFor(vehicleType);
        BigDecimal subtotal = BigDecimal.valueOf(routePlan.distanceKm()).multiply(perKm);
        if (subtotal.compareTo(MINIMUM_FARE) < 0) {
            subtotal = MINIMUM_FARE;
        }

        double demandMultiplier = round(1.0 + Math.min(0.85, demandSignal.score() * 0.55));
        BigDecimal surged = subtotal.multiply(BigDecimal.valueOf(demandMultiplier));
        BigDecimal modeAdjusted = surged.multiply(safeMode.fareMultiplier());
        BigDecimal discount = promoDiscount(modeAdjusted, promoCode);
        BigDecimal total = modeAdjusted.subtract(discount);
        if (total.compareTo(MINIMUM_FARE) < 0) {
            total = MINIMUM_FARE;
        }

        return new PricingQuote(
                subtotal.setScale(2, RoundingMode.HALF_UP),
                discount.setScale(2, RoundingMode.HALF_UP),
                total.setScale(2, RoundingMode.HALF_UP),
                demandMultiplier,
                demandSignal.zone(),
                demandSignal.label() + " in " + demandSignal.zone()
                        + "; " + safeMode.label() + " fare mode");
    }

    public BigDecimal promoDiscount(BigDecimal subtotal, String promoCode) {
        if (promoCode == null || promoCode.isBlank()) {
            return BigDecimal.ZERO;
        }
        String normalizedPromo = promoCode.trim().toUpperCase();
        if ("WELCOME10".equals(normalizedPromo)) {
            return subtotal.multiply(new BigDecimal("0.10"));
        }
        if ("CITY25".equals(normalizedPromo)) {
            return new BigDecimal("25.00");
        }
        if ("SHARE15".equals(normalizedPromo)) {
            return subtotal.multiply(new BigDecimal("0.15"));
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal rateFor(String vehicleType) {
        if (vehicleType == null) {
            return new BigDecimal("50");
        }
        return switch (vehicleType.trim().toUpperCase()) {
            case "MINI" -> new BigDecimal("42");
            case "SUV" -> new BigDecimal("68");
            case "PREMIUM" -> new BigDecimal("95");
            default -> new BigDecimal("50");
        };
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
