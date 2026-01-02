package com.hcmute.fme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyReportRequest {

    private Long scheduleId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Student email is required")
    private String studentEmail;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Shift is required")
    private String shift;

    @NotBlank(message = "Report content is required")
    private String report;

    private String title;

    private List<String> images;
}
