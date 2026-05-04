package com.cabbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_telemetry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double speedKmph;

    private Double fuelPercentage;

    private Double engineTemperatureC;

    private Double brakeIntensity;

    private Double acceleration;

    private String engineStatus;

    private String riskLevel;

    private Integer behaviorScore;

    private Boolean offlineBuffered;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    protected void onCreate() {
        if (recordedAt == null) {
            recordedAt = LocalDateTime.now();
        }
        if (offlineBuffered == null) {
            offlineBuffered = false;
        }
    }
}
