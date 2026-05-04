package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.mbb.module.map.Coordinate;
import com.cabbooking.mbb.module.map.HaversineDistanceCalculator;
import com.cabbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RideSharingOptimizer {
    private final BookingRepository bookingRepository;
    private final HaversineDistanceCalculator distanceCalculator;

    public RideSharingOptimizer(BookingRepository bookingRepository, HaversineDistanceCalculator distanceCalculator) {
        this.bookingRepository = bookingRepository;
        this.distanceCalculator = distanceCalculator;
    }

    public List<RideShareOption> findCompatibleRides(Coordinate pickup, Coordinate dropoff, Integer passengerCount) {
        int passengers = passengerCount == null ? 1 : passengerCount;
        if (passengers > 4) {
            return List.of();
        }

        return bookingRepository.findByStatusOrderByCreatedAtDesc(BookingStatus.PENDING).stream()
                .filter(booking -> Boolean.TRUE.equals(booking.getSharedRideOptIn()))
                .filter(booking -> (booking.getPassengerCount() == null ? 1 : booking.getPassengerCount()) + passengers <= 6)
                .map(booking -> toOption(booking, pickup, dropoff))
                .filter(option -> option.detourKm() <= 2.5)
                .sorted(Comparator.comparingDouble(RideShareOption::compatibilityScore).reversed())
                .limit(3)
                .toList();
    }

    private RideShareOption toOption(Booking booking, Coordinate pickup, Coordinate dropoff) {
        double pickupDetour = distanceCalculator.distanceKm(
                pickup.latitude(), pickup.longitude(), booking.getPickupLatitude(), booking.getPickupLongitude());
        double dropDetour = distanceCalculator.distanceKm(
                dropoff.latitude(), dropoff.longitude(), booking.getDropoffLatitude(), booking.getDropoffLongitude());
        double detour = pickupDetour + dropDetour;
        double score = Math.max(0, 1.0 - Math.min(detour, 4.0) / 4.0);
        return new RideShareOption(booking.getId(), booking.getPickupLocation(), booking.getDropoffLocation(),
                round(detour), round(score));
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
