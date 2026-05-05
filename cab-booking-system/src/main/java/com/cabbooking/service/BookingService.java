package com.cabbooking.service;

import com.cabbooking.entity.*;
import com.cabbooking.mbb.bridge.event.CabEventBridge;
import com.cabbooking.mbb.module.ai.AIDecisionService;
import com.cabbooking.mbb.module.ai.DriverMatchCandidate;
import com.cabbooking.mbb.module.ai.RideIntelligence;
import com.cabbooking.mbb.module.ai.RideMode;
import com.cabbooking.mbb.module.map.MapNavigationService;
import com.cabbooking.mbb.module.map.RoutePlan;
import com.cabbooking.repository.BookingRepository;
import com.cabbooking.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final DriverRepository driverRepository;
    private final LocationService locationService;
    private final MapNavigationService mapNavigationService;
    private final AIDecisionService aiDecisionService;
    private final CabEventBridge cabEventBridge;

    public Booking createBooking(User user, String pickupLocation, String dropoffLocation,
                                Double pickupLat, Double pickupLon,
                                Double dropoffLat, Double dropoffLon) {
        return createBooking(user, pickupLocation, dropoffLocation, pickupLat, pickupLon,
                dropoffLat, dropoffLon, "SEDAN", "RIDE_NOW", null,
                "CASH", 1, null, null);
    }

    public Booking createBooking(User user, String pickupLocation, String dropoffLocation,
                                Double pickupLat, Double pickupLon,
                                Double dropoffLat, Double dropoffLon,
                                String vehicleType, String rideType,
                                LocalDateTime scheduledPickupTime,
                                String paymentMethod, Integer passengerCount,
                                String promoCode, String specialInstructions) {
        return createBooking(user, pickupLocation, dropoffLocation, pickupLat, pickupLon,
                dropoffLat, dropoffLon, vehicleType, rideType, RideMode.BALANCED.name(), scheduledPickupTime,
                paymentMethod, passengerCount, promoCode, specialInstructions,
                false, false, true);
    }

    public Booking createBooking(User user, String pickupLocation, String dropoffLocation,
                                Double pickupLat, Double pickupLon,
                                Double dropoffLat, Double dropoffLon,
                                String vehicleType, String rideType, String rideMode,
                                LocalDateTime scheduledPickupTime,
                                String paymentMethod, Integer passengerCount,
                                String promoCode, String specialInstructions,
                                Boolean sharedRideOptIn, Boolean voiceAssisted,
                                Boolean offlineNavigationEnabled) {
        String normalizedVehicle = normalizeOption(vehicleType, "SEDAN");
        String normalizedRideType = normalizeOption(rideType, "RIDE_NOW");
        String normalizedRideMode = RideMode.from(rideMode).name();
        String normalizedPayment = normalizeOption(paymentMethod, "CASH");
        String normalizedPromo = normalizePromoCode(promoCode);
        Integer safePassengerCount = passengerCount == null || passengerCount < 1 ? 1 : passengerCount;
        boolean offlineEnabled = offlineNavigationEnabled == null || Boolean.TRUE.equals(offlineNavigationEnabled);

        RoutePlan routePlan = mapNavigationService.planRoute(
                pickupLocation, dropoffLocation, pickupLat, pickupLon, dropoffLat, dropoffLon,
                normalizedVehicle, offlineEnabled);
        RideIntelligence intelligence = aiDecisionService.evaluateRide(
                user, routePlan, normalizedVehicle, normalizedPromo, safePassengerCount,
                normalizedRideMode,
                scheduledPickupTime == null ? LocalDateTime.now() : scheduledPickupTime);

        Booking booking = Booking.builder()
                .user(user)
                .pickupLocation(pickupLocation)
                .dropoffLocation(dropoffLocation)
                .pickupLatitude(pickupLat)
                .pickupLongitude(pickupLon)
                .dropoffLatitude(dropoffLat)
                .dropoffLongitude(dropoffLon)
                .estimatedDistance(routePlan.distanceKm())
                .estimatedDuration(intelligence.adjustedDurationMinutes())
                .estimatedCost(intelligence.dynamicFare())
                .vehicleType(normalizedVehicle)
                .rideType(normalizedRideType)
                .rideMode(intelligence.rideMode())
                .rideModeLabel(intelligence.rideModeLabel())
                .rideModeReason(intelligence.rideModeReason())
                .scheduledPickupTime(scheduledPickupTime)
                .paymentMethod(normalizedPayment)
                .passengerCount(safePassengerCount)
                .promoCode(normalizedPromo)
                .sharedRideOptIn(Boolean.TRUE.equals(sharedRideOptIn))
                .voiceAssisted(Boolean.TRUE.equals(voiceAssisted))
                .offlineNavigationEnabled(routePlan.offlineFallbackAvailable())
                .specialInstructions(trimToNull(specialInstructions))
                .routeSummary(routePlan.routeSummary())
                .navigationInstructions(routePlan.instructionText())
                .routeAlgorithm(routePlan.algorithm())
                .demandMultiplier(intelligence.demandMultiplier())
                .demandZone(intelligence.demandZone())
                .fraudRiskScore(intelligence.fraudRiskScore())
                .fraudRiskLevel(intelligence.fraudRiskLevel())
                .fraudSignals(String.join("; ", intelligence.fraudReasons()))
                .smartRouteReason(intelligence.smartRouteReason())
                .rideShareSummary(summarizeShareOptions(intelligence))
                .matchConfidenceScore(intelligence.matchConfidenceScore())
                .ecoSavingsKg(intelligence.ecoSavingsKg())
                .status(BookingStatus.PENDING)
                .userRating(0.0)
                .build();

        Booking saved = bookingRepository.save(booking);
        cabEventBridge.bookingCreated(saved);
        return saved;
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Booking> getDriverBookings(Long driverId) {
        return bookingRepository.findByDriverId(driverId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<Booking> getPendingBookings() {
        return bookingRepository.findByStatusOrderByCreatedAtDesc(BookingStatus.PENDING);
    }

    public long countByStatus(BookingStatus status) {
        Long count = bookingRepository.countByStatus(status);
        return count == null ? 0 : count;
    }

    public BigDecimal getCompletedRevenue() {
        return bookingRepository.findByStatus(BookingStatus.COMPLETED)
                .stream()
                .map(booking -> booking.getActualCost() != null
                        ? booking.getActualCost()
                        : booking.getEstimatedCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Booking acceptBooking(Long bookingId, Driver driver) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in PENDING status");
        }
        booking.setDriver(driver);
        booking.setStatus(BookingStatus.ACCEPTED);
        driver.setStatus(DriverStatus.BUSY);
        driverRepository.save(driver);
        return bookingRepository.save(booking);
    }

    public Booking autoAssignDriver(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be auto-assigned");
        }

        RoutePlan routePlan = mapNavigationService.planRoute(
                booking.getPickupLocation(), booking.getDropoffLocation(),
                booking.getPickupLatitude(), booking.getPickupLongitude(),
                booking.getDropoffLatitude(), booking.getDropoffLongitude(),
                booking.getVehicleType(), Boolean.TRUE.equals(booking.getOfflineNavigationEnabled()));
        RideIntelligence intelligence = aiDecisionService.evaluateRide(
                booking.getUser(), routePlan, booking.getVehicleType(), booking.getPromoCode(),
                booking.getPassengerCount(), booking.getRideMode(), booking.getScheduledPickupTime());
        DriverMatchCandidate candidate = intelligence.driverCandidates().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available drivers found"));
        Driver driver = driverRepository.findById(candidate.driverId())
                .orElseThrow(() -> new RuntimeException("Matched driver no longer exists"));

        return acceptBooking(bookingId, driver);
    }

    public Booking startRide(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new RuntimeException("Booking must be ACCEPTED to start");
        }
        booking.setStatus(BookingStatus.STARTED);
        booking.setPickupTime(java.time.LocalDateTime.now());
        if (booking.getDriver() != null) {
            booking.getDriver().setStatus(DriverStatus.BUSY);
            driverRepository.save(booking.getDriver());
        }
        return bookingRepository.save(booking);
    }

    public Booking completeBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.STARTED) {
            throw new RuntimeException("Booking must be STARTED to complete");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setDropoffTime(java.time.LocalDateTime.now());
        booking.setActualCost(booking.getEstimatedCost());

        // Update driver rating
        Driver driver = booking.getDriver();
        if (driver != null) {
            Integer totalRides = driver.getTotalRides() + 1;
            driver.setTotalRides(totalRides);
            driver.setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId, String reason) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() == BookingStatus.COMPLETED ||
            booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel completed or already cancelled booking");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        if (booking.getDriver() != null) {
            booking.getDriver().setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(booking.getDriver());
        }
        return bookingRepository.save(booking);
    }

    public Booking rateBooking(Long bookingId, Double rating, String review) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only rate completed bookings");
        }
        booking.setUserRating(rating);
        booking.setUserReview(review);

        if (booking.getDriver() != null) {
            updateDriverRating(booking.getDriver().getId(), rating);
        }

        return bookingRepository.save(booking);
    }

    private void updateDriverRating(Long driverId, Double newRating) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver != null) {
            List<Booking> bookings = bookingRepository.findByDriverId(driverId);
            Double avgRating = bookings.stream()
                    .filter(b -> b.getUserRating() > 0)
                    .mapToDouble(Booking::getUserRating)
                    .average()
                    .orElse(5.0);
            driver.setRating(Math.round(avgRating * 10.0) / 10.0);
            driverRepository.save(driver);
        }
    }

    private String summarizeShareOptions(RideIntelligence intelligence) {
        if (intelligence.rideShareOptions().isEmpty()) {
            return "No compatible shared ride found yet";
        }
        return intelligence.rideShareOptions().stream()
                .map(option -> "#" + option.bookingId() + " detour " + option.detourKm() + " km")
                .collect(Collectors.joining("; "));
    }

    private String normalizeOption(String value, String fallback) {
        String normalized = trimToNull(value);
        return normalized == null ? fallback : normalized.trim().toUpperCase();
    }

    private String normalizePromoCode(String promoCode) {
        String normalized = trimToNull(promoCode);
        return normalized == null ? null : normalized.trim().toUpperCase();
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
