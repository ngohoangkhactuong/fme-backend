package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.entity.User;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.exception.UnauthorizedException;
import com.hcmute.fme.mapper.ScheduleMapper;
import com.hcmute.fme.repository.ScheduleRepository;
import com.hcmute.fme.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = Schedule.builder()
                .id(1L)
                .date(LocalDate.now())
                .shift("MORNING")
                .studentName("Student")
                .studentEmail("20190001@student.hcmute.edu.vn")
            .status(Schedule.ScheduleStatus.TODO)
                .build();
    }

    @Test
    void create_resolvesStudentNameAndSetsDefaults() {
        ScheduleRequest request = ScheduleRequest.builder()
                .date(LocalDate.now())
                .shift("MORNING")
                .studentEmail(schedule.getStudentEmail())
                .build();
        User user = User.builder().email(schedule.getStudentEmail()).name("Student").build();

        when(userRepository.findByEmail(schedule.getStudentEmail())).thenReturn(Optional.of(user));
        when(scheduleMapper.toEntity(any(ScheduleRequest.class))).thenReturn(schedule);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);
        when(scheduleMapper.toDTO(schedule)).thenReturn(new ScheduleDTO());

        scheduleService.create(request);

        ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(captor.capture());
        assertEquals(Schedule.ScheduleStatus.TODO, captor.getValue().getStatus());
        assertEquals("Student", request.getStudentName());
    }

    @Test
    void create_throwsWhenStudentEmailMissing() {
        ScheduleRequest request = ScheduleRequest.builder()
                .date(LocalDate.now())
                .shift("MORNING")
                .build();

        assertThrows(ResourceNotFoundException.class, () -> scheduleService.create(request));
    }

    @Test
    void update_usesExistingNameWhenMissing() {
        ScheduleRequest request = ScheduleRequest.builder()
                .date(LocalDate.now())
                .shift("MORNING")
                .studentEmail(schedule.getStudentEmail())
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(userRepository.findByEmail(schedule.getStudentEmail())).thenReturn(Optional.of(User.builder()
                .email(schedule.getStudentEmail())
                .name("Student")
                .role(User.Role.USER)
                .build()));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);
        when(scheduleMapper.toDTO(schedule)).thenReturn(new ScheduleDTO());

        scheduleService.update(1L, request);

        verify(scheduleMapper).updateEntity(eq(request), eq(schedule));
    }

    @Test
    void updateStatus_allowsOwner() {
        schedule.setStatus(Schedule.ScheduleStatus.TODO);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(userRepository.findByEmail(schedule.getStudentEmail())).thenReturn(Optional.of(User.builder()
                .email(schedule.getStudentEmail())
                .role(User.Role.USER)
                .build()));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);
        when(scheduleMapper.toDTO(schedule)).thenReturn(new ScheduleDTO());

        scheduleService.updateStatusForStudent(1L, schedule.getStudentEmail(), "IN_PROGRESS");

        assertEquals(Schedule.ScheduleStatus.IN_PROGRESS, schedule.getStatus());
    }

    @Test
    void updateStatus_rejectsAdmin() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(userRepository.findByEmail("admin@student.hcmute.edu.vn"))
                .thenReturn(Optional.of(User.builder()
                        .email("admin@student.hcmute.edu.vn")
                        .role(User.Role.ADMIN)
                        .build()));

        assertThrows(UnauthorizedException.class, () ->
                scheduleService.updateStatusForStudent(1L, "admin@student.hcmute.edu.vn", "IN_PROGRESS"));
    }

    @Test
    void updateStatus_rejectsNonOwner() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(userRepository.findByEmail("other@student.hcmute.edu.vn"))
                .thenReturn(Optional.of(User.builder()
                        .email("other@student.hcmute.edu.vn")
                        .role(User.Role.USER)
                        .build()));

        assertThrows(UnauthorizedException.class, () ->
                scheduleService.updateStatusForStudent(1L, "other@student.hcmute.edu.vn", "IN_PROGRESS"));
    }

    @Test
    void delete_throwsWhenMissing() {
        when(scheduleRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> scheduleService.delete(1L));
    }
}
