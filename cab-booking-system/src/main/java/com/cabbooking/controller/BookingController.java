package com.cabbooking.controller;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.User;
import com.cabbooking.mbb.module.ai.SmartRouteSuggestionService;
import com.cabbooking.service.BookingService;
import com.cabbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final SmartRouteSuggestionService smartRouteSuggestionService;

    @GetMapping("/book")
    public String showBookingForm(Authentication authentication, Model model) {
        if (authentication != null) {
            userService.findByEmail(authentication.getName())
                    .ifPresent(user -> model.addAttribute("smartSuggestions", smartRouteSuggestionService.suggestionsFor(user)));
        }
        return "booking/book";
    }

    @PostMapping("/create")
    public String createBooking(
            @RequestParam String pickupLocation,
            @RequestParam String dropoffLocation,
            @RequestParam Double pickupLatitude,
            @RequestParam Double pickupLongitude,
            @RequestParam Double dropoffLatitude,
            @RequestParam Double dropoffLongitude,
            @RequestParam(defaultValue = "SEDAN") String vehicleType,
            @RequestParam(defaultValue = "RIDE_NOW") String rideType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledPickupTime,
            @RequestParam(defaultValue = "CASH") String paymentMethod,
            @RequestParam(defaultValue = "1") Integer passengerCount,
            @RequestParam(required = false) String promoCode,
            @RequestParam(required = false) String specialInstructions,
            @RequestParam(required = false, defaultValue = "false") Boolean sharedRideOptIn,
            @RequestParam(required = false, defaultValue = "false") Boolean voiceAssisted,
            @RequestParam(required = false, defaultValue = "true") Boolean offlineNavigationEnabled,
            @RequestParam(required = false, defaultValue = "false") Boolean autoAssign,
            Authentication authentication,
            Model model) {
        try {
            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.createBooking(
                    user, pickupLocation, dropoffLocation,
                    pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude,
                    vehicleType, rideType, scheduledPickupTime, paymentMethod,
                    passengerCount, promoCode, specialInstructions,
                    sharedRideOptIn, voiceAssisted, offlineNavigationEnabled);

            if (Boolean.TRUE.equals(autoAssign)) {
                try {
                    booking = bookingService.autoAssignDriver(booking.getId());
                    return "redirect:/bookings/" + booking.getId() + "?matched";
                } catch (Exception ignored) {
                    return "redirect:/bookings/" + booking.getId() + "?queued";
                }
            }

            model.addAttribute("booking", booking);
            return "redirect:/bookings/" + booking.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "booking/book";
        }
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "booking/detail";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Booking> bookings = bookingService.getUserBookings(user);
        model.addAttribute("bookings", bookings);
        return "booking/my-bookings";
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, @RequestParam String reason, Model model) {
        try {
            bookingService.cancelBooking(id, reason);
            return "redirect:/bookings/" + id + "?cancelled";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/bookings/" + id;
        }
    }

    @GetMapping("/{id}/rate")
    public String showRatingForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "booking/rate";
    }

    @PostMapping("/{id}/rate")
    public String rateBooking(
            @PathVariable Long id,
            @RequestParam Double rating,
            @RequestParam String review,
            Model model) {
        try {
            bookingService.rateBooking(id, rating, review);
            return "redirect:/bookings/my-bookings?rated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "booking/rate";
        }
    }
}
