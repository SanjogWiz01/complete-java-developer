package com.cabbooking.mbb.module.ai;

public record DriverMatchCandidate(
        Long driverId,
        String driverName,
        String vehicleType,
        double distanceToPickupKm,
        int etaMinutes,
        double score,
        String reason) {
}
