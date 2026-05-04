package com.cabbooking.repository;

import com.cabbooking.entity.DriverTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DriverTelemetryRepository extends JpaRepository<DriverTelemetry, Long> {
    List<DriverTelemetry> findTop20ByOrderByRecordedAtDesc();

    List<DriverTelemetry> findByDriverIdAndRecordedAtAfterOrderByRecordedAtDesc(Long driverId, LocalDateTime recordedAt);
}
