package com.cabbooking.service;

import com.cabbooking.entity.*;
import com.cabbooking.repository.BookingRepository;
import com.cabbooking.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final DriverRepository driverRepository;
    private final LocationService locationService;

    public Booking createBooking(User user, String pickupLocation, String dropoffLocation,
                                Double pickupLat, Double pickupLon,
                                Double dropoffLat, Double dropoffLon) {
        Double distance = locationService.calculateDistance(
                pickupLat, pickupLon, dropoffLat, dropoffLon);
        Integer duration = locationService.estimateDuration(distance);
        var cost = locationService.calculateCost(distance, duration);

        Booking booking = Booking.builder()
                .user(user)
                .pickupLocation(pickupLocation)
                .dropoffLocation(dropoffLocation)
                .pickupLatitude(pickupLat)
                .pickupLongitude(pickupLon)
                .dropoffLatitude(dropoffLat)
                .dropoffLongitude(dropoffLon)
                .estimatedDistance(distance)
                .estimatedDuration(duration)
                .estimatedCost(cost)
                .status(BookingStatus.PENDING)
                .userRating(0.0)
                .build();

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getDriverBookings(Long driverId) {
        return bookingRepository.findByDriverId(driverId);
    }

    public List<Booking> getPendingBookings() {
        return bookingRepository.findByStatus(BookingStatus.PENDING);
    }

    public Booking acceptBooking(Long bookingId, Driver driver) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in PENDING status");
        }
        booking.setDriver(driver);
        booking.setStatus(BookingStatus.ACCEPTED);
        return bookingRepository.save(booking);
    }

    public Booking startRide(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new RuntimeException("Booking must be ACCEPTED to start");
        }
        booking.setStatus(BookingStatus.STARTED);
        booking.setPickupTime(java.time.LocalDateTime.now());
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
}
