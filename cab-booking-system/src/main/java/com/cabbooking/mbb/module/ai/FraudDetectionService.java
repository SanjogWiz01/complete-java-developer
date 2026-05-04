package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.User;
import com.cabbooking.mbb.module.map.RoutePlan;
import com.cabbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FraudDetectionService {
    private final BookingRepository bookingRepository;

    public FraudDetectionService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public FraudAssessment assess(User user, RoutePlan routePlan, String promoCode, Integer passengerCount) {
        List<String> reasons = new ArrayList<>();
        double score = 0.05;

        if (routePlan.distanceKm() > 35) {
            score += 0.18;
            reasons.add("Long-distance local ride");
        }
        if (passengerCount != null && passengerCount > 4) {
            score += 0.08;
            reasons.add("High passenger count");
        }
        if (promoCode != null && !promoCode.isBlank()) {
            score += 0.05;
        }
        if (user != null) {
            List<Booking> userBookings = bookingRepository.findByUserOrderByCreatedAtDesc(user);
            long cancelled = userBookings.stream()
                    .limit(10)
                    .filter(booking -> booking.getStatus() == BookingStatus.CANCELLED)
                    .count();
            long pending = userBookings.stream()
                    .filter(booking -> booking.getStatus() == BookingStatus.PENDING)
                    .count();
            if (cancelled >= 3) {
                score += 0.26;
                reasons.add("Repeated recent cancellations");
            }
            if (pending >= 3) {
                score += 0.18;
                reasons.add("Multiple pending requests");
            }
        }
        if (reasons.isEmpty()) {
            reasons.add("No strong anomaly signal");
        }

        double riskScore = Math.min(1.0, Math.round(score * 100.0) / 100.0);
        String level = riskScore >= 0.55 ? "HIGH" : riskScore >= 0.28 ? "MEDIUM" : "LOW";
        return new FraudAssessment(riskScore, level, reasons);
    }
}
