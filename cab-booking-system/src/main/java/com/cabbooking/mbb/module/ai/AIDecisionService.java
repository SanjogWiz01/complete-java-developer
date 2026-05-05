package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.User;
import com.cabbooking.mbb.module.map.Coordinate;
import com.cabbooking.mbb.module.map.RoutePlan;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AIDecisionService {
    private final DemandPredictionService demandPredictionService;
    private final DynamicPricingService dynamicPricingService;
    private final FraudDetectionService fraudDetectionService;
    private final DriverMatchingService driverMatchingService;
    private final RideSharingOptimizer rideSharingOptimizer;
    private final SmartRouteSuggestionService smartRouteSuggestionService;

    public AIDecisionService(DemandPredictionService demandPredictionService,
                             DynamicPricingService dynamicPricingService,
                             FraudDetectionService fraudDetectionService,
                             DriverMatchingService driverMatchingService,
                             RideSharingOptimizer rideSharingOptimizer,
                             SmartRouteSuggestionService smartRouteSuggestionService) {
        this.demandPredictionService = demandPredictionService;
        this.dynamicPricingService = dynamicPricingService;
        this.fraudDetectionService = fraudDetectionService;
        this.driverMatchingService = driverMatchingService;
        this.rideSharingOptimizer = rideSharingOptimizer;
        this.smartRouteSuggestionService = smartRouteSuggestionService;
    }

    public RideIntelligence evaluateRide(User user, RoutePlan routePlan, String vehicleType,
                                         String promoCode, Integer passengerCount,
                                         LocalDateTime requestedAt) {
        return evaluateRide(user, routePlan, vehicleType, promoCode, passengerCount,
                RideMode.BALANCED.name(), requestedAt);
    }

    public RideIntelligence evaluateRide(User user, RoutePlan routePlan, String vehicleType,
                                         String promoCode, Integer passengerCount,
                                         String rideMode, LocalDateTime requestedAt) {
        RideMode mode = RideMode.from(rideMode);
        Coordinate pickup = routePlan.pickup();
        DemandPredictionService.DemandSignal demandSignal = demandPredictionService.predict(pickup, requestedAt);
        PricingQuote pricingQuote = dynamicPricingService.quote(routePlan, vehicleType, promoCode, demandSignal, mode);
        FraudAssessment fraudAssessment = fraudDetectionService.assess(user, routePlan, promoCode, passengerCount);
        List<DriverMatchCandidate> driverCandidates = driverMatchingService.rankCandidates(pickup, vehicleType, mode);
        int adjustedDuration = mode.adjustDuration(routePlan.durationMinutes());
        double bestDriverScore = driverCandidates.stream()
                .findFirst()
                .map(DriverMatchCandidate::score)
                .orElse(0.0);
        double assuranceScore = mode.assuranceScore(
                bestDriverScore, fraudAssessment.riskScore(), routePlan.offlineFallbackAvailable());
        double ecoSavingsKg = mode.carbonSavingKg(routePlan.distanceKm(), vehicleType);
        String modeReason = mode.operationalReason(adjustedDuration, assuranceScore, ecoSavingsKg);

        return new RideIntelligence(
                pricingQuote.total(),
                pricingQuote.discount(),
                pricingQuote.demandMultiplier(),
                pricingQuote.demandZone(),
                demandSignal.label(),
                fraudAssessment.riskScore(),
                fraudAssessment.riskLevel(),
                fraudAssessment.reasons(),
                adjustedDuration,
                mode.name(),
                mode.label(),
                modeReason,
                assuranceScore,
                ecoSavingsKg,
                driverCandidates,
                rideSharingOptimizer.findCompatibleRides(pickup, routePlan.dropoff(), passengerCount),
                smartRouteSuggestionService.suggestionsFor(user),
                routePlan.offlineFallbackAvailable(),
                pricingQuote.explanation() + "; " + modeReason + "; " + routePlan.routeSummary());
    }
}
