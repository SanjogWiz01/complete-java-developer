package com.cabbooking.mbb.module.ai;

import java.math.BigDecimal;

public record PricingQuote(
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal total,
        double demandMultiplier,
        String demandZone,
        String explanation) {
}
