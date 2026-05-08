package com.cabbooking.mbb.bridge.event;

import java.time.LocalDateTime;

public record TrafficSignalUpdatedEvent(
        String eventType,
        Long aggregateId,
        String zone,
        String severity,
        LocalDateTime occurredAt) implements CabDomainEvent {
}
