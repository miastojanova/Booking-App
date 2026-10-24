package com.example.backend.repository;

import com.example.backend.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.startAt < :rangeEnd
          AND b.endAt > :rangeStart
        ORDER BY b.startAt ASC
        """)
    List<Booking> findOverlapping(
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd
    );

    @Query("""
        SELECT COUNT(b)
        FROM Booking b
        WHERE b.startAt < :rangeEnd
          AND b.endAt > :rangeStart
        """)
    long countOverlapping(
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd
    );
}
