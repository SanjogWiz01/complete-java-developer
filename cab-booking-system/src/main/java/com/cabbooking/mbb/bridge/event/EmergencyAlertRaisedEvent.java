package com.cabbooking.mbb.bridge.event;

import java.time.LocalDateTime;

public record EmergencyAlertRaisedEvent(
        String eventType,
        Long aggregateId,
        Long bookingId,
        Long userId,
        Double latitude,
        Double longitude,
        String severity,
        LocalDateTime occurredAt) implements CabDomainEvent {
}
