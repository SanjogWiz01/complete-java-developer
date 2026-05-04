package com.cabbooking.mbb.bridge.event;

import java.time.LocalDateTime;

public interface CabDomainEvent {
    String eventType();

    Long aggregateId();

    LocalDateTime occurredAt();
}
