package com.cabbooking.mbb.module.ai;

public record RideShareOption(
        Long bookingId,
        String pickupLocation,
        String dropoffLocation,
        double detourKm,
        double compatibilityScore) {
}
