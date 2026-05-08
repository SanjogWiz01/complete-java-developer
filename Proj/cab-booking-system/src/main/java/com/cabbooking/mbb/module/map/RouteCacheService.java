package com.cabbooking.mbb.module.map;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RouteCacheService {
    private static final Duration TTL = Duration.ofMinutes(12);

    private final ConcurrentMap<String, CachedRoute> routeCache = new ConcurrentHashMap<>();
    private final AtomicLong trafficRevision = new AtomicLong(1);

    public Optional<RoutePlan> get(String cacheKey) {
        CachedRoute cached = routeCache.get(cacheKey);
        if (cached == null) {
            return Optional.empty();
        }
        if (cached.createdAt().plus(TTL).isBefore(Instant.now())) {
            routeCache.remove(cacheKey);
            return Optional.empty();
        }
        return Optional.of(cached.routePlan().withCacheHit(true));
    }

    public RoutePlan put(String cacheKey, RoutePlan routePlan) {
        routeCache.put(cacheKey, new CachedRoute(routePlan.withCacheHit(false), Instant.now()));
        return routePlan;
    }

    public String buildKey(Coordinate pickup, Coordinate dropoff, String vehicleType, boolean offlineAllowed) {
        String normalizedVehicle = vehicleType == null || vehicleType.isBlank() ? "SEDAN" : vehicleType.trim().toUpperCase();
        return pickup.roundedKey() + "|" + dropoff.roundedKey() + "|" + normalizedVehicle + "|offline="
                + offlineAllowed + "|traffic=" + trafficRevision.get();
    }

    public long markTrafficChanged() {
        routeCache.clear();
        return trafficRevision.incrementAndGet();
    }

    public CacheStats stats() {
        return new CacheStats(routeCache.size(), trafficRevision.get(), TTL.toMinutes());
    }

    private record CachedRoute(RoutePlan routePlan, Instant createdAt) {
    }

    public record CacheStats(int cachedRoutes, long trafficRevision, long ttlMinutes) {
    }
}
