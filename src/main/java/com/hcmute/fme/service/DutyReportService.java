package com.hcmute.fme.service;

import com.hcmute.fme.dto.request.DutyReportRequest;
import com.hcmute.fme.dto.response.DutyReportDTO;
import com.hcmute.fme.entity.DutyReport;

import java.time.LocalDate;
import java.util.List;

public interface DutyReportService {

    DutyReportDTO create(DutyReportRequest request);

    DutyReportDTO getById(Long id);

    List<DutyReportDTO> getAll();

    List<DutyReportDTO> getByStudentEmail(String email);

    List<DutyReportDTO> getByScheduleId(Long scheduleId);

    List<DutyReportDTO> getByFilters(String studentEmail, LocalDate fromDate, LocalDate toDate);

    DutyReportDTO update(Long id, DutyReportRequest request);

    DutyReportDTO updateStatus(Long id, DutyReport.ReportStatus status);

    void delete(Long id);
}
