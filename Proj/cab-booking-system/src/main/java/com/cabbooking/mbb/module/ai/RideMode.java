package com.cabbooking.mbb.module.ai;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum RideMode {
    BALANCED("Smart Balance", "Balanced ETA, fare, route stability, and driver rating.",
            new BigDecimal("1.00"), 1.00, 0.74, 0.00),
    FASTEST("Fastest ETA", "Prioritizes closer drivers and lower arrival time.",
            new BigDecimal("1.12"), 0.88, 0.78, 0.00),
    ECONOMY("Economy", "Keeps the fare lower while preserving standard availability.",
            new BigDecimal("0.93"), 1.06, 0.70, 0.04),
    SAFETY("Safety+", "Prioritizes high-rated drivers and steadier route choices.",
            new BigDecimal("1.06"), 1.04, 0.88, 0.02),
    ECO("Eco Ride", "Prefers efficient vehicles and smoother lower-impact routing.",
            new BigDecimal("0.97"), 1.08, 0.76, 0.16);

    private final String label;
    private final String description;
    private final BigDecimal fareMultiplier;
    private final double durationMultiplier;
    private final double baseAssurance;
    private final double carbonSavingRate;

    RideMode(String label, String description, BigDecimal fareMultiplier,
             double durationMultiplier, double baseAssurance, double carbonSavingRate) {
        this.label = label;
        this.description = description;
        this.fareMultiplier = fareMultiplier;
        this.durationMultiplier = durationMultiplier;
        this.baseAssurance = baseAssurance;
        this.carbonSavingRate = carbonSavingRate;
    }

    public static RideMode from(String value) {
        if (value == null || value.isBlank()) {
            return BALANCED;
        }
        try {
            return RideMode.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return BALANCED;
        }
    }

    public String label() {
        return label;
    }

    public String description() {
        return description;
    }

    public BigDecimal fareMultiplier() {
        return fareMultiplier;
    }

    public int adjustDuration(int routeMinutes) {
        return Math.max(1, (int) Math.ceil(routeMinutes * durationMultiplier));
    }

    public double assuranceScore(double bestDriverScore, double fraudRiskScore, boolean offlineNavigationAvailable) {
        double fraudPenalty = Math.min(0.22, fraudRiskScore * 0.18);
        double offlineBoost = offlineNavigationAvailable ? 0.03 : 0.0;
        double score = baseAssurance + (bestDriverScore * 0.16) + offlineBoost - fraudPenalty;
        return round(Math.max(0.35, Math.min(0.99, score)));
    }

    public double carbonSavingKg(double distanceKm, String vehicleType) {
        double vehicleRate = "MINI".equalsIgnoreCase(vehicleType) ? carbonSavingRate + 0.03 : carbonSavingRate;
        return round(Math.max(0, distanceKm * vehicleRate));
    }

    public String operationalReason(int adjustedDurationMinutes, double assuranceScore, double carbonSavingKg) {
        return label + " selected: " + description + " ETA "
                + adjustedDurationMinutes + " min, assurance "
                + Math.round(assuranceScore * 100) + "%, eco saving "
                + BigDecimal.valueOf(carbonSavingKg).setScale(2, RoundingMode.HALF_UP) + " kg.";
    }

    public boolean isFastest() {
        return this == FASTEST;
    }

    public boolean isSafetyFocused() {
        return this == SAFETY;
    }

    public boolean isCostFocused() {
        return this == ECONOMY;
    }

    public boolean isEcoFocused() {
        return this == ECO;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
