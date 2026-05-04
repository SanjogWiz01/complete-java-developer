package com.cabbooking.repository;

import com.cabbooking.entity.EmergencyAlert;
import com.cabbooking.entity.EmergencyAlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, Long> {
    List<EmergencyAlert> findTop20ByOrderByCreatedAtDesc();

    long countByStatus(EmergencyAlertStatus status);
}
