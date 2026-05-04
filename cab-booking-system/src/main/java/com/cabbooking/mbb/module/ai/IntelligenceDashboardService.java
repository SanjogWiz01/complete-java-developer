package com.cabbooking.mbb.module.ai;

import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.EmergencyAlertStatus;
import com.cabbooking.mbb.module.map.MapNavigationService;
import com.cabbooking.mbb.module.map.RouteCacheService;
import com.cabbooking.repository.BookingRepository;
import com.cabbooking.repository.DriverTelemetryRepository;
import com.cabbooking.repository.EmergencyAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IntelligenceDashboardService {
    private final DemandPredictionService demandPredictionService;
    private final MapNavigationService mapNavigationService;
    private final BookingRepository bookingRepository;
    private final DriverTelemetryRepository telemetryRepository;
    private final EmergencyAlertRepository emergencyAlertRepository;

    public IntelligenceDashboardService(DemandPredictionService demandPredictionService,
                                        MapNavigationService mapNavigationService,
                                        BookingRepository bookingRepository,
                                        DriverTelemetryRepository telemetryRepository,
                                        EmergencyAlertRepository emergencyAlertRepository) {
        this.demandPredictionService = demandPredictionService;
        this.mapNavigationService = mapNavigationService;
        this.bookingRepository = bookingRepository;
        this.telemetryRepository = telemetryRepository;
        this.emergencyAlertRepository = emergencyAlertRepository;
    }

    public DashboardSnapshot snapshot() {
        RouteCacheService.CacheStats cacheStats = mapNavigationService.cacheStats();
        long activeRides = bookingRepository.countByStatus(BookingStatus.ACCEPTED)
                + bookingRepository.countByStatus(BookingStatus.STARTED);
        return new DashboardSnapshot(
                demandPredictionService.heatmap(),
                telemetryRepository.findTop20ByOrderByRecordedAtDesc(),
                emergencyAlertRepository.findTop20ByOrderByCreatedAtDesc(),
                Map.of(
                        "Map & Navigation", "Online: A* graph, Haversine, cache " + cacheStats.cachedRoutes(),
                        "AI Optimization", "Online: demand, matching, pricing, fraud scoring",
                        "IoT/Circuit Bridge", "Online: telemetry endpoint, behavior scoring",
                        "Safety", "Online: SOS events " + emergencyAlertRepository.countByStatus(EmergencyAlertStatus.OPEN)
                ),
                cacheStats.cachedRoutes(),
                cacheStats.trafficRevision(),
                activeRides);
    }

    public record DashboardSnapshot(
            List<DemandHeatmapPoint> heatmap,
            List<com.cabbooking.entity.DriverTelemetry> latestTelemetry,
            List<com.cabbooking.entity.EmergencyAlert> emergencyAlerts,
            Map<String, String> moduleHealth,
            int cachedRoutes,
            long trafficRevision,
            long activeRides) {
    }
}
