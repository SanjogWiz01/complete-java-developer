package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.BookingStatus;
import com.cabbooking.mbb.module.map.Coordinate;
import com.cabbooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class DemandPredictionService {
    private final BookingRepository bookingRepository;

    public DemandPredictionService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public DemandSignal predict(Coordinate pickup, LocalDateTime requestedAt) {
        DemandHeatmapPoint nearest = baseHeatmap().stream()
                .min(Comparator.comparingDouble(point -> distanceScore(pickup, point)))
                .orElse(baseHeatmap().get(0));
        double timeWeight = timeWeight(requestedAt);
        long activeLoad = bookingRepository.countByStatus(BookingStatus.PENDING)
                + bookingRepository.countByStatus(BookingStatus.ACCEPTED)
                + bookingRepository.countByStatus(BookingStatus.STARTED);
        double loadWeight = Math.min(0.25, activeLoad * 0.03);
        double score = Math.min(1.0, nearest.score() + timeWeight + loadWeight);
        String label = score > 0.78 ? "High demand" : score > 0.48 ? "Moderate demand" : "Balanced demand";
        return new DemandSignal(nearest.zone(), round(score), label);
    }

    public List<DemandHeatmapPoint> heatmap() {
        return baseHeatmap().stream()
                .map(point -> new DemandHeatmapPoint(point.zone(), point.latitude(), point.longitude(),
                        round(Math.min(1.0, point.score() + liveLoadBoost(point.zone()))), point.label()))
                .sorted(Comparator.comparingDouble(DemandHeatmapPoint::score).reversed())
                .toList();
    }

    private List<DemandHeatmapPoint> baseHeatmap() {
        return List.of(
                new DemandHeatmapPoint("LAKESIDE", 28.2096, 83.9856, 0.64, "Tourist pickup corridor"),
                new DemandHeatmapPoint("AIRPORT", 28.2009, 83.9821, 0.72, "Flight arrival cluster"),
                new DemandHeatmapPoint("PRITHVI", 28.2130, 83.9973, 0.58, "Bus and market transfers"),
                new DemandHeatmapPoint("SARANGKOT", 28.2439, 83.9483, 0.46, "Sunrise tourism window"),
                new DemandHeatmapPoint("BINDHYABASINI", 28.2333, 83.9833, 0.40, "Temple and north city trips")
        );
    }

    private double liveLoadBoost(String zone) {
        String zoneToken = zone.toLowerCase();
        return bookingRepository.findAllByOrderByCreatedAtDesc().stream()
                .limit(50)
                .filter(booking -> booking.getPickupLocation() != null
                        && booking.getPickupLocation().toLowerCase().contains(zoneToken))
                .count() * 0.02;
    }

    private double distanceScore(Coordinate pickup, DemandHeatmapPoint point) {
        double lat = pickup.latitude() - point.latitude();
        double lng = pickup.longitude() - point.longitude();
        return Math.sqrt(lat * lat + lng * lng);
    }

    private double timeWeight(LocalDateTime requestedAt) {
        int hour = requestedAt == null ? LocalDateTime.now().getHour() : requestedAt.getHour();
        if ((hour >= 7 && hour <= 10) || (hour >= 17 && hour <= 20)) {
            return 0.18;
        }
        if (hour >= 21 || hour <= 5) {
            return 0.12;
        }
        return 0.05;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public record DemandSignal(String zone, double score, String label) {
    }
}
