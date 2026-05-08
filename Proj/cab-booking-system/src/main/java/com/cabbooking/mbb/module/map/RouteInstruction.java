package com.cabbooking.mbb.module.map;

public record RouteInstruction(int sequence, String maneuver, String landmark, double distanceKm) {
}
