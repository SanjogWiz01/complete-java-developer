package com.cabbooking.controller.api;

import com.cabbooking.mbb.module.iot.VehicleTelemetryRequest;
import com.cabbooking.mbb.module.iot.VehicleTelemetryResponse;
import com.cabbooking.mbb.module.iot.VehicleTelemetryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/iot")
public class IotBridgeController {
    private final VehicleTelemetryService telemetryService;
    private final String expectedToken;

    public IotBridgeController(VehicleTelemetryService telemetryService,
                               @Value("${cab.mbb.iot.token:dev-iot-token}") String expectedToken) {
        this.telemetryService = telemetryService;
        this.expectedToken = expectedToken;
    }

    @PostMapping("/telemetry")
    public ResponseEntity<VehicleTelemetryResponse> receiveTelemetry(
            @RequestHeader(name = "X-IOT-TOKEN", required = false) String token,
            @RequestBody VehicleTelemetryRequest request) {
        if (!expectedToken.equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new VehicleTelemetryResponse(null, request.driverId(), "UNKNOWN", 0,
                            false, "Invalid IoT bridge token"));
        }
        return ResponseEntity.ok(telemetryService.record(request));
    }
}
