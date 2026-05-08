package com.cabbooking.mbb.module.iot;

import java.time.LocalDateTime;

public record VehicleTelemetryRequest(
        Long driverId,
        Double latitude,
        Double longitude,
        Double speedKmph,
        Double fuelPercentage,
        Double engineTemperatureC,
        Double brakeIntensity,
        Double acceleration,
        String engineStatus,
        Boolean offlineBuffered,
        LocalDateTime recordedAt) {
}
