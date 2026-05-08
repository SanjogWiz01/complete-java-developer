package com.cabbooking.mbb.module.iot;

public record VehicleTelemetryResponse(
        Long telemetryId,
        Long driverId,
        String riskLevel,
        Integer behaviorScore,
        boolean locationEventPublished,
        String message) {
}
