package com.hcmute.fme.repository;

import com.hcmute.fme.entity.DutyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DutyReportRepository extends JpaRepository<DutyReport, Long> {
    
    List<DutyReport> findByStudentEmailOrderBySubmittedAtDesc(String studentEmail);
    
    List<DutyReport> findByScheduleIdOrderBySubmittedAtDesc(Long scheduleId);
    
    @Query("SELECT d FROM DutyReport d WHERE d.submittedAt BETWEEN :from AND :to ORDER BY d.submittedAt DESC")
    List<DutyReport> findByDateRange(
            @Param("from") LocalDateTime from, 
            @Param("to") LocalDateTime to);
    
    @Query("SELECT d FROM DutyReport d WHERE " +
           "(:studentEmail IS NULL OR LOWER(d.studentEmail) LIKE LOWER(CONCAT('%', :studentEmail, '%'))) AND " +
           "(:fromDate IS NULL OR d.date >= :fromDate) AND " +
           "(:toDate IS NULL OR d.date <= :toDate) " +
           "ORDER BY d.submittedAt DESC")
    List<DutyReport> findByFilters(
            @Param("studentEmail") String studentEmail,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);
    
    List<DutyReport> findByStatusOrderBySubmittedAtDesc(DutyReport.ReportStatus status);
}
