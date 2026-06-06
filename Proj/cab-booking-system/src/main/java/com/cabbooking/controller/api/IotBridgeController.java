package com.cabbooking.controller.api;

import com.cabbooking.mbb.module.iot.VehicleTelemetryRequest;
import com.cabbooking.mbb.module.iot.VehicleTelemetryResponse;
import com.cabbooking.mbb.module.iot.VehicleTelemetryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/iot")
public class IotBridgeController {
    private final VehicleTelemetryService telemetryService;
    private final String expectedToken;

    public IotBridgeController(VehicleTelemetryService telemetryService,
                               @Value("${cab.mbb.iot.token:}") String expectedToken) {
        this.telemetryService = telemetryService;
        this.expectedToken = expectedToken;
    }

    @PostMapping("/telemetry")
    public ResponseEntity<VehicleTelemetryResponse> receiveTelemetry(
            @RequestHeader(name = "X-IOT-TOKEN", required = false) String token,
            @RequestBody VehicleTelemetryRequest request) {
        if (!hasValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new VehicleTelemetryResponse(null, request.driverId(), "UNKNOWN", 0,
                            false, "Invalid IoT bridge token"));
        }
        return ResponseEntity.ok(telemetryService.record(request));
    }

    private boolean hasValidToken(String token) {
        if (!StringUtils.hasText(expectedToken) || token == null) {
            return false;
        }
        byte[] expectedBytes = expectedToken.getBytes(StandardCharsets.UTF_8);
        byte[] providedBytes = token.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expectedBytes, providedBytes);
    }
}
