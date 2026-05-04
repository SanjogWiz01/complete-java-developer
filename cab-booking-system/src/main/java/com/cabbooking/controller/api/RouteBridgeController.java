package com.cabbooking.controller.api;

import com.cabbooking.entity.User;
import com.cabbooking.mbb.module.ai.AIDecisionService;
import com.cabbooking.mbb.module.ai.DriverMatchCandidate;
import com.cabbooking.mbb.module.ai.RideIntelligence;
import com.cabbooking.mbb.module.ai.RideShareOption;
import com.cabbooking.mbb.module.map.MapNavigationService;
import com.cabbooking.mbb.module.map.RouteInstruction;
import com.cabbooking.mbb.module.map.RoutePlan;
import com.cabbooking.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteBridgeController {
    private final MapNavigationService mapNavigationService;
    private final AIDecisionService aiDecisionService;
    private final UserService userService;

    public RouteBridgeController(MapNavigationService mapNavigationService,
                                 AIDecisionService aiDecisionService,
                                 UserService userService) {
        this.mapNavigationService = mapNavigationService;
        this.aiDecisionService = aiDecisionService;
        this.userService = userService;
    }

    @GetMapping("/preview")
    public RoutePreviewResponse preview(@RequestParam String pickupLocation,
                                        @RequestParam String dropoffLocation,
                                        @RequestParam Double pickupLatitude,
                                        @RequestParam Double pickupLongitude,
                                        @RequestParam Double dropoffLatitude,
                                        @RequestParam Double dropoffLongitude,
                                        @RequestParam(defaultValue = "SEDAN") String vehicleType,
                                        @RequestParam(required = false) String promoCode,
                                        @RequestParam(defaultValue = "1") Integer passengerCount,
                                        @RequestParam(defaultValue = "true") Boolean offlineNavigationEnabled,
                                        Authentication authentication) {
        User user = authentication == null ? null
                : userService.findByEmail(authentication.getName()).orElse(null);
        RoutePlan routePlan = mapNavigationService.planRoute(
                pickupLocation, dropoffLocation,
                pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude,
                vehicleType, Boolean.TRUE.equals(offlineNavigationEnabled));
        RideIntelligence intelligence = aiDecisionService.evaluateRide(
                user, routePlan, vehicleType, promoCode, passengerCount, LocalDateTime.now());

        return new RoutePreviewResponse(
                routePlan.distanceKm(),
                routePlan.durationMinutes(),
                intelligence.dynamicFare(),
                intelligence.promoDiscount(),
                intelligence.demandMultiplier(),
                intelligence.demandZone(),
                intelligence.demandLabel(),
                intelligence.fraudRiskScore(),
                intelligence.fraudRiskLevel(),
                routePlan.routeSummary(),
                routePlan.algorithm(),
                routePlan.cacheHit(),
                routePlan.offlineFallbackAvailable(),
                routePlan.latLngPairs(),
                routePlan.instructions().stream().map(NavigationStep::from).toList(),
                intelligence.driverCandidates(),
                intelligence.rideShareOptions(),
                intelligence.smartSuggestions(),
                intelligence.smartRouteReason());
    }

    public record RoutePreviewResponse(
            double distanceKm,
            int durationMinutes,
            BigDecimal estimatedFare,
            BigDecimal promoDiscount,
            double demandMultiplier,
            String demandZone,
            String demandLabel,
            double fraudRiskScore,
            String fraudRiskLevel,
            String routeSummary,
            String algorithm,
            boolean cacheHit,
            boolean offlineFallbackAvailable,
            List<List<Double>> routeCoordinates,
            List<NavigationStep> instructions,
            List<DriverMatchCandidate> driverCandidates,
            List<RideShareOption> rideShareOptions,
            List<String> smartSuggestions,
            String smartRouteReason) {
    }

    public record NavigationStep(int sequence, String maneuver, String landmark, double distanceKm) {
        static NavigationStep from(RouteInstruction instruction) {
            return new NavigationStep(instruction.sequence(), instruction.maneuver(),
                    instruction.landmark(), instruction.distanceKm());
        }
    }
}
