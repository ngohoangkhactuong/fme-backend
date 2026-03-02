package com.hcmute.fme.dto.request;

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
public class DutyReportDraftRequest {

    @NotNull(message = "Schedule is required")
    private Long scheduleId;

    private String title;

    private String report;

    private List<String> images;
}
