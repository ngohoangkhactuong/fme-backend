package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.ScheduleMapper;
import com.hcmute.fme.repository.ScheduleRepository;
import com.hcmute.fme.repository.UserRepository;
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
class ScheduleServiceImplAdditionalTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Test
    void update_throwsWhenMissing() {
        ScheduleRequest request = ScheduleRequest.builder()
                .date(LocalDate.now())
                .shift("MORNING")
                .studentEmail("20190001@student.hcmute.edu.vn")
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> scheduleService.update(1L, request));
    }

    @Test
    void updateStatus_throwsWhenMissingSchedule() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            scheduleService.updateStatusForStudent(1L, "admin", "IN_PROGRESS"));
    }

    @Test
    void getById_throwsWhenMissing() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> scheduleService.getById(1L));
    }

    @Test
    void delete_throwsWhenMissingSchedule() {
        when(scheduleRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> scheduleService.delete(1L));
    }
}
