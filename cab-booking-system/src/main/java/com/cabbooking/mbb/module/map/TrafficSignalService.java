package com.cabbooking.mbb.module.map;

import com.cabbooking.mbb.bridge.event.CabEventBridge;
import org.springframework.stereotype.Service;

@Service
public class TrafficSignalService {
    private final CabEventBridge cabEventBridge;

    public TrafficSignalService(CabEventBridge cabEventBridge) {
        this.cabEventBridge = cabEventBridge;
    }

    public void signalTrafficChange(String zone, String severity) {
        cabEventBridge.trafficSignalUpdated(
                zone == null || zone.isBlank() ? "POKHARA" : zone.trim().toUpperCase(),
                severity == null || severity.isBlank() ? "MEDIUM" : severity.trim().toUpperCase());
    }
}
