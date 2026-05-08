package com.cabbooking.mbb.bridge.event;

import java.time.LocalDateTime;

public record BookingCreatedEvent(
        String eventType,
        Long aggregateId,
        Long userId,
        Double pickupLatitude,
        Double pickupLongitude,
        Double dropoffLatitude,
        Double dropoffLongitude,
        String vehicleType,
        String rideType,
        LocalDateTime occurredAt) implements CabDomainEvent {
}
