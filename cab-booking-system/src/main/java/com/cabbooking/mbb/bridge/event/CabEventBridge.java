package com.cabbooking.mbb.bridge.event;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.Driver;
import com.cabbooking.entity.DriverTelemetry;
import com.cabbooking.entity.EmergencyAlert;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CabEventBridge {
    private final ApplicationEventPublisher publisher;

    public CabEventBridge(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void bookingCreated(Booking booking) {
        publisher.publishEvent(new BookingCreatedEvent(
                "booking.created",
                booking.getId(),
                booking.getUser() == null ? null : booking.getUser().getId(),
                booking.getPickupLatitude(),
                booking.getPickupLongitude(),
                booking.getDropoffLatitude(),
                booking.getDropoffLongitude(),
                booking.getVehicleType(),
                booking.getRideType(),
                LocalDateTime.now()));
    }

    public void driverLocationChanged(Driver driver, Double previousLatitude, Double previousLongitude, String source) {
        publisher.publishEvent(new DriverLocationChangedEvent(
                "driver.location.changed",
                driver.getId(),
                previousLatitude,
                previousLongitude,
                driver.getCurrentLatitude(),
                driver.getCurrentLongitude(),
                source,
                LocalDateTime.now()));
    }

    public void telemetryReceived(DriverTelemetry telemetry) {
        publisher.publishEvent(new VehicleTelemetryReceivedEvent(
                "iot.telemetry.received",
                telemetry.getDriver().getId(),
                telemetry.getLatitude(),
                telemetry.getLongitude(),
                telemetry.getSpeedKmph(),
                telemetry.getFuelPercentage(),
                telemetry.getEngineTemperatureC(),
                telemetry.getRiskLevel(),
                LocalDateTime.now()));
    }

    public void emergencyRaised(EmergencyAlert alert) {
        publisher.publishEvent(new EmergencyAlertRaisedEvent(
                "safety.sos.raised",
                alert.getId(),
                alert.getBooking() == null ? null : alert.getBooking().getId(),
                alert.getUser() == null ? null : alert.getUser().getId(),
                alert.getLatitude(),
                alert.getLongitude(),
                alert.getSeverity(),
                LocalDateTime.now()));
    }

    public void trafficSignalUpdated(String zone, String severity) {
        publisher.publishEvent(new TrafficSignalUpdatedEvent(
                "traffic.signal.updated",
                0L,
                zone,
                severity,
                LocalDateTime.now()));
    }
}
