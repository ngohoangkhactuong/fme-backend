package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.DutyReportRequest;
import com.hcmute.fme.dto.response.DutyReportDTO;
import com.hcmute.fme.entity.DutyReport;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.DutyReportMapper;
import com.hcmute.fme.repository.DutyReportRepository;
import com.hcmute.fme.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DutyReportServiceImplAdditionalTest {

    @Mock
    private DutyReportRepository dutyReportRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private DutyReportMapper dutyReportMapper;

    @InjectMocks
    private DutyReportServiceImpl dutyReportService;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = Schedule.builder()
                .id(1L)
                .studentEmail("20190001@student.hcmute.edu.vn")
                .studentName("Student")
                .date(LocalDate.now())
                .shift("MORNING")
                .build();
    }

    @Test
    void update_throwsWhenMissing() {
        DutyReportRequest request = DutyReportRequest.builder()
                .scheduleId(1L)
                .title("Title")
                .report("Report")
                .build();

        when(dutyReportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dutyReportService.update(1L, request));
    }

    @Test
    void update_throwsWhenScheduleMissing() {
        DutyReportRequest request = DutyReportRequest.builder()
                .scheduleId(1L)
                .title("Title")
                .report("Report")
                .build();
        DutyReport report = DutyReport.builder().id(1L).build();

        when(dutyReportRepository.findById(1L)).thenReturn(Optional.of(report));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dutyReportService.update(1L, request));
    }

    @Test
    void updateStatus_throwsWhenMissing() {
        when(dutyReportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                dutyReportService.updateStatus(1L, DutyReport.ReportStatus.APPROVED));
    }
}
