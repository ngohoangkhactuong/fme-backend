package com.hcmute.fme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyReportDTO {

    private Long id;
    private Long scheduleId;
    private String studentName;
    private String studentEmail;
    private LocalDate date;
    private String shift;
    private String report;
    private String title;
    private String status;
    private List<String> images;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
