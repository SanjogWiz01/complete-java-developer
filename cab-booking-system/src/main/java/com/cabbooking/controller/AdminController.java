package com.cabbooking.controller;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.Driver;
import com.cabbooking.entity.User;
import com.cabbooking.service.BookingService;
import com.cabbooking.service.DriverService;
import com.cabbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final DriverService driverService;
    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Driver> drivers = driverService.getAllDrivers();
        List<Booking> bookings = bookingService.getPendingBookings();
        model.addAttribute("drivers", drivers);
        model.addAttribute("bookings", bookings);
        model.addAttribute("totalDrivers", drivers.size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        return "admin/dashboard";
    }

    @GetMapping("/drivers")
    public String listDrivers(Model model) {
        List<Driver> drivers = driverService.getAllDrivers();
        model.addAttribute("drivers", drivers);
        return "admin/drivers";
    }

    @GetMapping("/drivers/{id}")
    public String viewDriver(@PathVariable Long id, Model model) {
        Driver driver = driverService.getDriverById(id);
        List<Booking> bookings = bookingService.getDriverBookings(id);
        model.addAttribute("driver", driver);
        model.addAttribute("bookings", bookings);
        return "admin/driver-detail";
    }

    @GetMapping("/bookings")
    public String listBookings(Model model) {
        List<Booking> bookings = bookingService.getPendingBookings();
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }

    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        model.addAttribute("booking", booking);
        model.addAttribute("availableDrivers", availableDrivers);
        return "admin/booking-detail";
    }

    @PostMapping("/bookings/{id}/assign/{driverId}")
    public String assignDriver(
            @PathVariable Long id,
            @PathVariable Long driverId,
            Model model) {
        try {
            Booking booking = bookingService.getBookingById(id);
            Driver driver = driverService.getDriverById(driverId);
            bookingService.acceptBooking(id, driver);
            return "redirect:/admin/bookings/" + id + "?assigned";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/bookings/" + id;
        }
    }

    @PostMapping("/bookings/{id}/start")
    public String startRide(@PathVariable Long id, Model model) {
        try {
            bookingService.startRide(id);
            return "redirect:/admin/bookings/" + id + "?started";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/bookings/" + id;
        }
    }

    @PostMapping("/bookings/{id}/complete")
    public String completeRide(@PathVariable Long id, Model model) {
        try {
            bookingService.completeBooking(id);
            return "redirect:/admin/bookings/" + id + "?completed";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/bookings/" + id;
        }
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
}
