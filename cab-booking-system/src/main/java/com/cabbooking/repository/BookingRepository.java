package com.cabbooking.repository;

import com.cabbooking.entity.Booking;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
    List<Booking> findByDriverId(Long driverId);
}
