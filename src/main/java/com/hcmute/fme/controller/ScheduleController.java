package com.hcmute.fme.controller;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ApiResponse;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "Duty schedule management APIs")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "Create a new schedule (Admin only)")
    public ResponseEntity<ApiResponse<ScheduleDTO>> create(@Valid @RequestBody ScheduleRequest request) {
        ScheduleDTO schedule = scheduleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Schedule created successfully", schedule));
    }

    @GetMapping
    @Operation(summary = "Get all schedules")
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getAll() {
        List<ScheduleDTO> schedules = scheduleService.getAll();
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID")
    public ResponseEntity<ApiResponse<ScheduleDTO>> getById(@PathVariable Long id) {
        ScheduleDTO schedule = scheduleService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(schedule));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get schedules by date")
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ScheduleDTO> schedules = scheduleService.getByDate(date);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/range")
    @Operation(summary = "Get schedules by date range")
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ScheduleDTO> schedules = scheduleService.getByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @GetMapping("/student/{email}")
    @Operation(summary = "Get schedules by student email")
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getByStudentEmail(@PathVariable String email) {
        List<ScheduleDTO> schedules = scheduleService.getByStudentEmail(email);
        return ResponseEntity.ok(ApiResponse.success(schedules));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update schedule (Admin only)")
    public ResponseEntity<ApiResponse<ScheduleDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequest request) {
        ScheduleDTO schedule = scheduleService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Schedule updated successfully", schedule));
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "Confirm schedule (Admin only)")
    public ResponseEntity<ApiResponse<ScheduleDTO>> confirm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        ScheduleDTO schedule = scheduleService.confirm(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Schedule confirmed successfully", schedule));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete schedule (Admin only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Schedule deleted successfully", null));
    }
}
