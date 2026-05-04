package com.cabbooking.controller;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.Driver;
import com.cabbooking.entity.DriverStatus;
import com.cabbooking.entity.User;
import com.cabbooking.mbb.module.ai.IntelligenceDashboardService;
import com.cabbooking.mbb.module.map.TrafficSignalService;
import com.cabbooking.mbb.module.safety.EmergencyAlertService;
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
    private final IntelligenceDashboardService intelligenceDashboardService;
    private final TrafficSignalService trafficSignalService;
    private final EmergencyAlertService emergencyAlertService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Driver> drivers = driverService.getAllDrivers();
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("drivers", drivers);
        model.addAttribute("bookings", bookings);
        model.addAttribute("totalDrivers", drivers.size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("pendingBookings", bookingService.countByStatus(BookingStatus.PENDING));
        model.addAttribute("activeBookings",
                bookingService.countByStatus(BookingStatus.ACCEPTED)
                        + bookingService.countByStatus(BookingStatus.STARTED));
        model.addAttribute("completedBookings", bookingService.countByStatus(BookingStatus.COMPLETED));
        model.addAttribute("cancelledBookings", bookingService.countByStatus(BookingStatus.CANCELLED));
        model.addAttribute("completedRevenue", bookingService.getCompletedRevenue());
        model.addAttribute("intelligenceSnapshot", intelligenceDashboardService.snapshot());
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
        model.addAttribute("statuses", DriverStatus.values());
        return "admin/driver-detail";
    }

    @PostMapping("/drivers/{id}/status")
    public String updateDriverStatus(
            @PathVariable Long id,
            @RequestParam DriverStatus status) {
        driverService.updateDriverStatus(id, status);
        return "redirect:/admin/drivers/" + id + "?statusUpdated";
    }

    @GetMapping("/bookings")
    public String listBookings(@RequestParam(required = false) BookingStatus status, Model model) {
        List<Booking> bookings = status == null
                ? bookingService.getAllBookings()
                : bookingService.getBookingsByStatus(status);
        model.addAttribute("bookings", bookings);
        model.addAttribute("statuses", BookingStatus.values());
        model.addAttribute("selectedStatus", status);
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

    @PostMapping("/bookings/{id}/auto-assign")
    public String autoAssignDriver(@PathVariable Long id, Model model) {
        try {
            bookingService.autoAssignDriver(id);
            return "redirect:/admin/bookings/" + id + "?assigned";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/bookings/" + id + "?assignmentError";
        }
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

    @GetMapping("/intelligence")
    public String intelligence(Model model) {
        model.addAttribute("snapshot", intelligenceDashboardService.snapshot());
        return "admin/intelligence";
    }

    @PostMapping("/traffic/signal")
    public String signalTraffic(@RequestParam String zone,
                                @RequestParam(defaultValue = "MEDIUM") String severity) {
        trafficSignalService.signalTrafficChange(zone, severity);
        return "redirect:/admin/intelligence?trafficUpdated";
    }

    @PostMapping("/alerts/{id}/acknowledge")
    public String acknowledgeAlert(@PathVariable Long id) {
        emergencyAlertService.acknowledge(id);
        return "redirect:/admin/intelligence?alertAcknowledged";
    }

    @PostMapping("/alerts/{id}/resolve")
    public String resolveAlert(@PathVariable Long id) {
        emergencyAlertService.resolve(id);
        return "redirect:/admin/intelligence?alertResolved";
    }
}
