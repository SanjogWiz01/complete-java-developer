package com.cabbooking.repository;

import com.cabbooking.entity.Driver;
import com.cabbooking.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByStatus(DriverStatus status);
    Driver findByUserId(Long userId);
}
