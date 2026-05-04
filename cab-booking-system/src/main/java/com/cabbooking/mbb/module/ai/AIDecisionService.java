package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.User;
import com.cabbooking.mbb.module.map.Coordinate;
import com.cabbooking.mbb.module.map.RoutePlan;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        Coordinate pickup = routePlan.pickup();
        DemandPredictionService.DemandSignal demandSignal = demandPredictionService.predict(pickup, requestedAt);
        PricingQuote pricingQuote = dynamicPricingService.quote(routePlan, vehicleType, promoCode, demandSignal);
        FraudAssessment fraudAssessment = fraudDetectionService.assess(user, routePlan, promoCode, passengerCount);

        return new RideIntelligence(
                pricingQuote.total(),
                pricingQuote.discount(),
                pricingQuote.demandMultiplier(),
                pricingQuote.demandZone(),
                demandSignal.label(),
                fraudAssessment.riskScore(),
                fraudAssessment.riskLevel(),
                fraudAssessment.reasons(),
                driverMatchingService.rankCandidates(pickup, vehicleType),
                rideSharingOptimizer.findCompatibleRides(pickup, routePlan.dropoff(), passengerCount),
                smartRouteSuggestionService.suggestionsFor(user),
                routePlan.offlineFallbackAvailable(),
                pricingQuote.explanation() + "; " + routePlan.routeSummary());
    }
}
