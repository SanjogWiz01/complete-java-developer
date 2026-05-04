package com.cabbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String dropoffLocation;

    @Column(nullable = false)
    private Double pickupLatitude;

    @Column(nullable = false)
    private Double pickupLongitude;

    @Column(nullable = false)
    private Double dropoffLatitude;

    @Column(nullable = false)
    private Double dropoffLongitude;

    @Column(nullable = false)
    private Double estimatedDistance; // in km

    @Column(nullable = false)
    private Integer estimatedDuration; // in minutes

    @Column(nullable = false)
    private BigDecimal estimatedCost;

    private String vehicleType;

    private String rideType;

    private LocalDateTime scheduledPickupTime;

    private String paymentMethod;

    private Integer passengerCount;

    private String promoCode;

    private Boolean sharedRideOptIn;

    private Boolean voiceAssisted;

    private Boolean offlineNavigationEnabled;

    @Column(length = 700)
    private String specialInstructions;

    @Column(length = 1200)
    private String routeSummary;

    @Column(length = 2500)
    private String navigationInstructions;

    private String routeAlgorithm;

    private Double demandMultiplier;

    private String demandZone;

    private Double fraudRiskScore;

    private String fraudRiskLevel;

    @Column(length = 900)
    private String fraudSignals;

    @Column(length = 900)
    private String smartRouteReason;

    @Column(length = 900)
    private String rideShareSummary;

    @Column(nullable = true)
    private BigDecimal actualCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status; // PENDING, ACCEPTED, STARTED, COMPLETED, CANCELLED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "dropoff_time")
    private LocalDateTime dropoffTime;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String cancellationReason;

    @Column(nullable = false)
    private Double userRating; // 0 to 5

    @Column(length = 500)
    private String userReview;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        userRating = 0.0;
        if (sharedRideOptIn == null) {
            sharedRideOptIn = false;
        }
        if (voiceAssisted == null) {
            voiceAssisted = false;
        }
        if (offlineNavigationEnabled == null) {
            offlineNavigationEnabled = false;
        }
        if (demandMultiplier == null) {
            demandMultiplier = 1.0;
        }
        if (fraudRiskScore == null) {
            fraudRiskScore = 0.0;
        }
        if (fraudRiskLevel == null) {
            fraudRiskLevel = "LOW";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
