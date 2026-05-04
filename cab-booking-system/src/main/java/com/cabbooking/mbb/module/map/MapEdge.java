package com.cabbooking.mbb.module.map;

public record MapEdge(String fromNodeId, String toNodeId, double distanceKm, double trafficWeight) {
    public double weightedDistance() {
        return distanceKm * trafficWeight;
    }
}
