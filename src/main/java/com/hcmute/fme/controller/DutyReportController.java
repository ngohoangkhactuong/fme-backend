package com.hcmute.fme.controller;

import com.hcmute.fme.dto.request.DutyReportRequest;
import com.hcmute.fme.dto.response.ApiResponse;
import com.hcmute.fme.dto.response.DutyReportDTO;
import com.hcmute.fme.entity.DutyReport;
import com.hcmute.fme.service.DutyReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Duty Report", description = "Duty report management APIs")
public class DutyReportController {

    private final DutyReportService dutyReportService;

    @PostMapping
    @Operation(summary = "Create a new duty report")
    public ResponseEntity<ApiResponse<DutyReportDTO>> create(@Valid @RequestBody DutyReportRequest request) {
        DutyReportDTO report = dutyReportService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Report created successfully", report));
    }

    @GetMapping
    @Operation(summary = "Get all reports")
    public ResponseEntity<ApiResponse<List<DutyReportDTO>>> getAll() {
        List<DutyReportDTO> reports = dutyReportService.getAll();
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get report by ID")
    public ResponseEntity<ApiResponse<DutyReportDTO>> getById(@PathVariable Long id) {
        DutyReportDTO report = dutyReportService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/student/{email}")
    @Operation(summary = "Get reports by student email")
    public ResponseEntity<ApiResponse<List<DutyReportDTO>>> getByStudentEmail(@PathVariable String email) {
        List<DutyReportDTO> reports = dutyReportService.getByStudentEmail(email);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/schedule/{scheduleId}")
    @Operation(summary = "Get reports by schedule ID")
    public ResponseEntity<ApiResponse<List<DutyReportDTO>>> getByScheduleId(@PathVariable Long scheduleId) {
        List<DutyReportDTO> reports = dutyReportService.getByScheduleId(scheduleId);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/filter")
    @Operation(summary = "Get reports with filters")
    public ResponseEntity<ApiResponse<List<DutyReportDTO>>> getByFilters(
            @RequestParam(required = false) String studentEmail,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<DutyReportDTO> reports = dutyReportService.getByFilters(studentEmail, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update report")
    public ResponseEntity<ApiResponse<DutyReportDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody DutyReportRequest request) {
        DutyReportDTO report = dutyReportService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Report updated successfully", report));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update report status (Admin only)")
    public ResponseEntity<ApiResponse<DutyReportDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam DutyReport.ReportStatus status) {
        DutyReportDTO report = dutyReportService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Report status updated successfully", report));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete report")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        dutyReportService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Report deleted successfully", null));
    }
}
