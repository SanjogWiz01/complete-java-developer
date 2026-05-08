package com.cabbooking.mbb.module.iot;

import org.springframework.stereotype.Component;

@Component
public class DriverBehaviorMonitor {
    public BehaviorAssessment assess(VehicleTelemetryRequest request) {
        int score = 100;
        if (safe(request.speedKmph()) > 70) {
            score -= 22;
        }
        if (safe(request.brakeIntensity()) > 0.75) {
            score -= 18;
        }
        if (Math.abs(safe(request.acceleration())) > 3.0) {
            score -= 12;
        }
        if (safe(request.engineTemperatureC()) > 105) {
            score -= 20;
        }
        if (safe(request.fuelPercentage()) > 0 && safe(request.fuelPercentage()) < 12) {
            score -= 10;
        }

        int normalized = Math.max(0, Math.min(100, score));
        String level = normalized < 55 ? "HIGH" : normalized < 76 ? "MEDIUM" : "LOW";
        return new BehaviorAssessment(normalized, level);
    }

    private double safe(Double value) {
        return value == null ? 0 : value;
    }

    public record BehaviorAssessment(int behaviorScore, String riskLevel) {
    }
}
