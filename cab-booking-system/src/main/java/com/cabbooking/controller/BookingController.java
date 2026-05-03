package com.cabbooking.controller;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.User;
import com.cabbooking.service.BookingService;
import com.cabbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/book")
    public String showBookingForm() {
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
            Authentication authentication,
            Model model) {
        try {
            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Booking booking = bookingService.createBooking(
                    user, pickupLocation, dropoffLocation,
                    pickupLatitude, pickupLongitude,
                    dropoffLatitude, dropoffLongitude);

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
