package com.cabbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private String severity;

    @Column(length = 700)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencyAlertStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = EmergencyAlertStatus.OPEN;
        }
        if (severity == null || severity.isBlank()) {
            severity = "HIGH";
        }
    }
}
