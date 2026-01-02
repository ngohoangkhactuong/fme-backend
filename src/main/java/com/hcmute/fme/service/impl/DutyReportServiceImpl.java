package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.DutyReportRequest;
import com.hcmute.fme.dto.response.DutyReportDTO;
import com.hcmute.fme.entity.DutyReport;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.DutyReportMapper;
import com.hcmute.fme.repository.DutyReportRepository;
import com.hcmute.fme.repository.ScheduleRepository;
import com.hcmute.fme.service.DutyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DutyReportServiceImpl implements DutyReportService {

    private final DutyReportRepository dutyReportRepository;
    private final ScheduleRepository scheduleRepository;
    private final DutyReportMapper dutyReportMapper;

    @Override
    @Transactional
    public DutyReportDTO create(DutyReportRequest request) {
        DutyReport dutyReport = dutyReportMapper.toEntity(request);
        dutyReport.setSubmittedAt(LocalDateTime.now());
        dutyReport.setStatus(DutyReport.ReportStatus.PENDING);

        if (request.getScheduleId() != null) {
            Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", request.getScheduleId()));
            dutyReport.setSchedule(schedule);
        }

        dutyReport = dutyReportRepository.save(dutyReport);
        return dutyReportMapper.toDTO(dutyReport);
    }

    @Override
    @Transactional(readOnly = true)
    public DutyReportDTO getById(Long id) {
        DutyReport dutyReport = dutyReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DutyReport", "id", id));
        return dutyReportMapper.toDTO(dutyReport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutyReportDTO> getAll() {
        List<DutyReport> reports = dutyReportRepository.findAll();
        return dutyReportMapper.toDTOList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutyReportDTO> getByStudentEmail(String email) {
        List<DutyReport> reports = dutyReportRepository.findByStudentEmailOrderBySubmittedAtDesc(email);
        return dutyReportMapper.toDTOList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutyReportDTO> getByScheduleId(Long scheduleId) {
        List<DutyReport> reports = dutyReportRepository.findByScheduleIdOrderBySubmittedAtDesc(scheduleId);
        return dutyReportMapper.toDTOList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutyReportDTO> getByFilters(String studentEmail, LocalDate fromDate, LocalDate toDate) {
        List<DutyReport> reports = dutyReportRepository.findByFilters(studentEmail, fromDate, toDate);
        return dutyReportMapper.toDTOList(reports);
    }

    @Override
    @Transactional
    public DutyReportDTO update(Long id, DutyReportRequest request) {
        DutyReport dutyReport = dutyReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DutyReport", "id", id));

        dutyReportMapper.updateEntity(request, dutyReport);

        if (request.getScheduleId() != null) {
            Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", request.getScheduleId()));
            dutyReport.setSchedule(schedule);
        }

        dutyReport = dutyReportRepository.save(dutyReport);
        return dutyReportMapper.toDTO(dutyReport);
    }

    @Override
    @Transactional
    public DutyReportDTO updateStatus(Long id, DutyReport.ReportStatus status) {
        DutyReport dutyReport = dutyReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DutyReport", "id", id));

        dutyReport.setStatus(status);
        dutyReport = dutyReportRepository.save(dutyReport);
        return dutyReportMapper.toDTO(dutyReport);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!dutyReportRepository.existsById(id)) {
            throw new ResourceNotFoundException("DutyReport", "id", id);
        }
        dutyReportRepository.deleteById(id);
    }
}
