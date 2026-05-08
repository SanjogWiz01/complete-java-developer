package com.cabbooking.mbb.bridge.event;

import java.time.LocalDateTime;

public record DriverLocationChangedEvent(
        String eventType,
        Long aggregateId,
        Double previousLatitude,
        Double previousLongitude,
        Double currentLatitude,
        Double currentLongitude,
        String signalSource,
        LocalDateTime occurredAt) implements CabDomainEvent {
}
