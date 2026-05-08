package com.cabbooking.mbb.module.ai;

import java.util.List;

public record FraudAssessment(double riskScore, String riskLevel, List<String> reasons) {
    public FraudAssessment {
        reasons = List.copyOf(reasons);
    }
}
