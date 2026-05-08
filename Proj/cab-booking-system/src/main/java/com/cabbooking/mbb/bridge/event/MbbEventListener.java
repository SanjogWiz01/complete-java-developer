package com.cabbooking.mbb.bridge.event;

import com.cabbooking.mbb.module.map.MapNavigationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MbbEventListener {
    private static final Logger log = LoggerFactory.getLogger(MbbEventListener.class);

    private final MapNavigationService mapNavigationService;

    public MbbEventListener(MapNavigationService mapNavigationService) {
        this.mapNavigationService = mapNavigationService;
    }

    @EventListener
    public void onTrafficSignal(TrafficSignalUpdatedEvent event) {
        long revision = mapNavigationService.handleTrafficSignalChange();
        log.info("Traffic signal changed for {} severity {}; route cache revision {}",
                event.zone(), event.severity(), revision);
    }

    @EventListener
    public void onDriverLocationChanged(DriverLocationChangedEvent event) {
        log.debug("Driver {} moved from {},{} to {},{} via {}",
                event.aggregateId(),
                event.previousLatitude(),
                event.previousLongitude(),
                event.currentLatitude(),
                event.currentLongitude(),
                event.signalSource());
    }

    @EventListener
    public void onBookingCreated(BookingCreatedEvent event) {
        log.debug("Booking {} entered MBB event bridge for {} route processing", event.aggregateId(), event.vehicleType());
    }

    @EventListener
    public void onEmergencyAlert(EmergencyAlertRaisedEvent event) {
        log.warn("SOS alert {} for booking {} at {},{}",
                event.aggregateId(), event.bookingId(), event.latitude(), event.longitude());
    }
}
