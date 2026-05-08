package com.cabbooking.mbb.bridge.event;

import java.time.LocalDateTime;

public record VehicleTelemetryReceivedEvent(
        String eventType,
        Long aggregateId,
        Double latitude,
        Double longitude,
        Double speedKmph,
        Double fuelPercentage,
        Double engineTemperatureC,
        String riskLevel,
        LocalDateTime occurredAt) implements CabDomainEvent {
}
