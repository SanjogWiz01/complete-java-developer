package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.User;
import com.cabbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SmartRouteSuggestionService {
    private final BookingRepository bookingRepository;

    public SmartRouteSuggestionService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<String> suggestionsFor(User user) {
        if (user == null) {
            return defaultSuggestions();
        }
        List<String> history = bookingRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .limit(4)
                .map(this::routeLabel)
                .filter(Objects::nonNull)
                .toList();
        if (history.isEmpty()) {
            return defaultSuggestions();
        }
        return history;
    }

    public List<String> defaultSuggestions() {
        return List.of(
                "Lakeside to Pokhara International Airport",
                "Prithvi Chowk to Bindhyabasini Temple",
                "Phewa Lake to Sarangkot"
        );
    }

    private String routeLabel(Booking booking) {
        if (booking.getPickupLocation() == null || booking.getDropoffLocation() == null) {
            return null;
        }
        return booking.getPickupLocation() + " to " + booking.getDropoffLocation();
    }
}
