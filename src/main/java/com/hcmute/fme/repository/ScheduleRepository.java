package com.hcmute.fme.repository;

import com.hcmute.fme.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findByDateOrderByShiftAsc(LocalDate date);
    
    List<Schedule> findByDateBetweenOrderByDateAscShiftAsc(LocalDate startDate, LocalDate endDate);
    
    List<Schedule> findByStudentEmailOrderByDateDesc(String studentEmail);
    
    @Query("SELECT s FROM Schedule s WHERE s.date >= :date AND s.isConfirmed = false ORDER BY s.date ASC")
    List<Schedule> findUpcomingUnconfirmed(@Param("date") LocalDate date);
    
    @Query("SELECT s FROM Schedule s WHERE s.studentEmail = :email AND s.date = :date AND s.shift = :shift")
    List<Schedule> findByStudentEmailAndDateAndShift(
            @Param("email") String email, 
            @Param("date") LocalDate date, 
            @Param("shift") String shift);
}
