# CabBook MBB Architecture

This project uses a Module-Block-Bridge layout inside the existing Spring Boot cab management system. Modules own business capability, blocks keep the logic small, and bridges move data through REST endpoints, MVC actions, Spring domain events, and repositories.

## Layers

| Layer | Responsibility | Implementation |
| --- | --- | --- |
| Data Layer | Bookings, drivers, users, GPS, IoT telemetry, SOS alerts | JPA entities and repositories |
| Logic Layer | Route planning, AI optimization, matching, pricing, safety decisions | `com.cabbooking.mbb.module.*` services |
| Presentation Layer | Booking UI, admin dashboards, map views, route instructions | Thymeleaf, Bootstrap, Leaflet, event-driven JavaScript |

## Modules And Blocks

### User Module
- Registration, login, booking, cancellation, ratings.
- Smart route suggestions from user ride history.
- Booking options for ride-sharing, voice-assisted trips, and offline fallback navigation.

### Driver Module
- Driver registration, status management, location updates.
- Driver movement publishes `driver.location.changed` only after meaningful movement.
- Driver telemetry updates can refresh the driver location through the IoT bridge.

### Admin Module
- Booking, user, and driver management.
- `/admin/intelligence` shows module health, predictive demand, route cache, IoT telemetry, and SOS alerts.
- Traffic signal form publishes an event that invalidates route cache instead of using background polling.

### Map & Navigation Module
Package: `com.cabbooking.mbb.module.map`

Blocks:
- `CoordinateParser` resolves typed labels and popular Pokhara stops.
- `CityGraphMap` stores the landmark graph.
- `PathfindingEngine` runs A* over the graph.
- `HaversineDistanceCalculator` calculates real-world distance.
- `RouteInstructionDecoder` produces turn-by-turn, landmark-based instructions.
- `RouteCacheService` caches route plans by rounded coordinates, vehicle, offline mode, and traffic revision.
- `MapNavigationService` bridges those blocks into a single route planning API.

### AI Optimization Module
Package: `com.cabbooking.mbb.module.ai`

Blocks:
- `DriverMatchingService` ranks available drivers by proximity, rating, and vehicle fit.
- `DemandPredictionService` creates demand scores and heatmap points.
- `DynamicPricingService` calculates demand-aware fare.
- `FraudDetectionService` scores suspicious booking patterns.
- `RideSharingOptimizer` finds compatible pending shared rides.
- `SmartRouteSuggestionService` learns simple route suggestions from booking history.
- `AIDecisionService` combines the AI blocks into one booking decision.

### IoT/Circuit Integration Module
Package: `com.cabbooking.mbb.module.iot`

Blocks:
- `IotBridgeController` receives lightweight telemetry at `POST /api/iot/telemetry`.
- `VehicleTelemetryService` persists telemetry and emits events.
- `DriverBehaviorMonitor` scores speed, braking, acceleration, fuel, and engine heat.
- `DriverTelemetry` stores GPS, fuel, engine, and behavior data.

Use header `X-IOT-TOKEN: dev-iot-token` for local testing. Change `cab.mbb.iot.token` before deployment.

### Safety Module
Package: `com.cabbooking.mbb.module.safety`

Blocks:
- `EmergencyAlertService` creates and updates SOS alerts.
- `SafetyController` lets riders trigger SOS from active booking details.
- Admin can acknowledge or resolve alerts from `/admin/intelligence`.

## Bridges

| Bridge | Direction | Purpose |
| --- | --- | --- |
| `/api/routes/preview` | UI -> Logic | Event-style route preview when user changes meaningful trip inputs |
| `/api/iot/telemetry` | Circuit/IoT -> Data/Logic | Vehicle GPS, fuel, engine, and behavior signal ingestion |
| Spring events | Module -> Module | Booking created, driver moved, telemetry received, SOS raised, traffic updated |
| MVC forms | UI -> Logic | Booking, SOS, traffic signal, admin actions |
| Repositories | Logic -> Data | Persistence without coupling modules to UI |

## Event-Driven Flow

1. User changes a pickup, drop-off, vehicle, promo, passenger count, or offline mode.
2. JavaScript debounces the change and calls `/api/routes/preview`.
3. Map module plans a cached A* route and decodes instructions.
4. AI module returns demand pricing, matching, fraud, sharing, and smart suggestions.
5. Booking confirmation persists the decision and publishes `booking.created`.
6. IoT telemetry or driver app movement publishes `driver.location.changed` only after meaningful movement.
7. Traffic signal updates publish `traffic.signal.updated`, which clears route cache by revision.

There are no polling loops or infinite recalculation paths. Recalculation happens from user action, telemetry, driver movement, or admin traffic signal.

## Scalability Notes

- Route cache keys include traffic revision so stale routes are invalidated cheaply.
- Modules are package-isolated and communicate through bridges, making AI models or map providers replaceable.
- IoT ingestion is token-protected and DTO-based, so it can move behind MQTT, Kafka, or a gateway later.
- AI services are deterministic now, but the `AIDecisionService` facade is ready for external model calls or larger datasets.
- Admin intelligence keeps operations visible without mixing dashboard UI into core logic.
