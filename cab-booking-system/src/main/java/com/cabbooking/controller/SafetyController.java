package com.cabbooking.controller;

import com.cabbooking.entity.User;
import com.cabbooking.mbb.module.safety.EmergencyAlertService;
import com.cabbooking.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class SafetyController {
    private final EmergencyAlertService emergencyAlertService;
    private final UserService userService;

    public SafetyController(EmergencyAlertService emergencyAlertService, UserService userService) {
        this.emergencyAlertService = emergencyAlertService;
        this.userService = userService;
    }

    @PostMapping("/{id}/sos")
    public String raiseSos(@PathVariable Long id,
                           @RequestParam(required = false) String message,
                           Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        emergencyAlertService.raiseForBooking(id, user, message);
        return "redirect:/bookings/" + id + "?sos";
    }
}
