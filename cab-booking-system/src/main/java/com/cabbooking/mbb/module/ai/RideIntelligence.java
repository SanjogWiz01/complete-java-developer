package com.cabbooking.mbb.module.ai;

import java.math.BigDecimal;
import java.util.List;

public record RideIntelligence(
        BigDecimal dynamicFare,
        BigDecimal promoDiscount,
        double demandMultiplier,
        String demandZone,
        String demandLabel,
        double fraudRiskScore,
        String fraudRiskLevel,
        List<String> fraudReasons,
        List<DriverMatchCandidate> driverCandidates,
        List<RideShareOption> rideShareOptions,
        List<String> smartSuggestions,
        boolean offlineNavigationAvailable,
        String smartRouteReason) {

    public RideIntelligence {
        fraudReasons = List.copyOf(fraudReasons);
        driverCandidates = List.copyOf(driverCandidates);
        rideShareOptions = List.copyOf(rideShareOptions);
        smartSuggestions = List.copyOf(smartSuggestions);
    }
}
