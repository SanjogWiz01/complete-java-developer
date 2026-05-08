package com.cabbooking.mbb.module.safety;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.EmergencyAlert;
import com.cabbooking.entity.EmergencyAlertStatus;
import com.cabbooking.entity.User;
import com.cabbooking.mbb.bridge.event.CabEventBridge;
import com.cabbooking.repository.BookingRepository;
import com.cabbooking.repository.EmergencyAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmergencyAlertService {
    private final BookingRepository bookingRepository;
    private final EmergencyAlertRepository emergencyAlertRepository;
    private final CabEventBridge cabEventBridge;

    public EmergencyAlertService(BookingRepository bookingRepository,
                                 EmergencyAlertRepository emergencyAlertRepository,
                                 CabEventBridge cabEventBridge) {
        this.bookingRepository = bookingRepository;
        this.emergencyAlertRepository = emergencyAlertRepository;
        this.cabEventBridge = cabEventBridge;
    }

    public EmergencyAlert raiseForBooking(Long bookingId, User user, String message) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        EmergencyAlert alert = EmergencyAlert.builder()
                .booking(booking)
                .user(user)
                .latitude(booking.getDriver() == null ? booking.getPickupLatitude() : booking.getDriver().getCurrentLatitude())
                .longitude(booking.getDriver() == null ? booking.getPickupLongitude() : booking.getDriver().getCurrentLongitude())
                .severity("HIGH")
                .message(message == null || message.isBlank() ? "Rider triggered SOS alert" : message.trim())
                .status(EmergencyAlertStatus.OPEN)
                .build();
        EmergencyAlert saved = emergencyAlertRepository.save(alert);
        cabEventBridge.emergencyRaised(saved);
        return saved;
    }

    public EmergencyAlert acknowledge(Long alertId) {
        EmergencyAlert alert = get(alertId);
        alert.setStatus(EmergencyAlertStatus.ACKNOWLEDGED);
        return emergencyAlertRepository.save(alert);
    }

    public EmergencyAlert resolve(Long alertId) {
        EmergencyAlert alert = get(alertId);
        alert.setStatus(EmergencyAlertStatus.RESOLVED);
        alert.setResolvedAt(LocalDateTime.now());
        return emergencyAlertRepository.save(alert);
    }

    public List<EmergencyAlert> latestAlerts() {
        return emergencyAlertRepository.findTop20ByOrderByCreatedAtDesc();
    }

    private EmergencyAlert get(Long alertId) {
        return emergencyAlertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
    }
}
