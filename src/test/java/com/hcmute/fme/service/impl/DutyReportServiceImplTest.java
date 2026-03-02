package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.DutyReportDraftRequest;
import com.hcmute.fme.dto.request.DutyReportRequest;
import com.hcmute.fme.dto.response.DutyReportDTO;
import com.hcmute.fme.entity.DutyReport;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.exception.UnauthorizedException;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DutyReportServiceImplTest {

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
    void createForStudent_rejectsWrongStudent() {
        DutyReportRequest request = DutyReportRequest.builder()
                .scheduleId(1L)
                .title("Title")
                .report("Report")
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        assertThrows(UnauthorizedException.class, () ->
                dutyReportService.createForStudent(request, "other@student.hcmute.edu.vn"));
    }

    @Test
    void createForStudent_promotesDraftToPending() {
        DutyReportRequest request = DutyReportRequest.builder()
                .scheduleId(1L)
                .title("Title")
                .report("Report")
                .images(List.of("img"))
                .build();
        DutyReport draft = DutyReport.builder()
                .id(10L)
                .status(DutyReport.ReportStatus.DRAFT)
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(dutyReportRepository.findByScheduleIdAndStudentEmailAndStatus(
                1L, schedule.getStudentEmail(), DutyReport.ReportStatus.DRAFT))
                .thenReturn(Optional.of(draft));
        when(dutyReportRepository.save(any(DutyReport.class))).thenReturn(draft);
        when(dutyReportMapper.toDTO(any(DutyReport.class))).thenReturn(new DutyReportDTO());

        dutyReportService.createForStudent(request, schedule.getStudentEmail());

        assertEquals(DutyReport.ReportStatus.PENDING, draft.getStatus());
    }

    @Test
    void saveDraftForStudent_createsDraft() {
        DutyReportDraftRequest request = DutyReportDraftRequest.builder()
                .scheduleId(1L)
                .title("Draft")
                .report("{}")
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(dutyReportRepository.findByScheduleIdAndStudentEmailAndStatus(
                1L, schedule.getStudentEmail(), DutyReport.ReportStatus.DRAFT))
                .thenReturn(Optional.empty());
        when(dutyReportRepository.save(any(DutyReport.class))).thenReturn(new DutyReport());
        when(dutyReportMapper.toDTO(any(DutyReport.class))).thenReturn(new DutyReportDTO());

        dutyReportService.saveDraftForStudent(request, schedule.getStudentEmail());

        verify(dutyReportRepository).save(any(DutyReport.class));
    }

    @Test
    void saveDraftForStudent_rejectsOtherStudent() {
        DutyReportDraftRequest request = DutyReportDraftRequest.builder()
                .scheduleId(1L)
                .title("Draft")
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        assertThrows(UnauthorizedException.class, () ->
                dutyReportService.saveDraftForStudent(request, "other@student.hcmute.edu.vn"));
    }

    @Test
    void getDraftForStudent_throwsWhenMissing() {
        when(dutyReportRepository.findByScheduleIdAndStudentEmailAndStatus(
                anyLong(), any(), eq(DutyReport.ReportStatus.DRAFT)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                dutyReportService.getDraftForStudent(1L, schedule.getStudentEmail()));
    }
}
