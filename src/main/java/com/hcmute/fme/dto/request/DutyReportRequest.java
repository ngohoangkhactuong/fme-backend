package com.hcmute.fme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyReportRequest {

    @NotNull(message = "Schedule is required")
    private Long scheduleId;

    @NotBlank(message = "Report content is required")
    private String report;

    private String title;

    private List<String> images;
}
