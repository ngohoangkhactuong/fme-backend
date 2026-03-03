package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.exception.UnauthorizedException;
import com.hcmute.fme.exception.ApiException;
import com.hcmute.fme.mapper.ScheduleMapper;
import com.hcmute.fme.entity.User;
import com.hcmute.fme.repository.ScheduleRepository;
import com.hcmute.fme.repository.UserRepository;
import com.hcmute.fme.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ScheduleDTO create(ScheduleRequest request) {
        String studentName = resolveStudentName(request.getStudentName(), request.getStudentEmail());
        request.setStudentName(studentName);
        Schedule schedule = scheduleMapper.toEntity(request);
        attachUserIfExists(schedule, request.getStudentEmail());
        schedule.setStatus(Schedule.ScheduleStatus.TODO);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDTO getById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getByDate(LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByDateOrderByShiftAsc(date);
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = scheduleRepository.findByDateBetweenOrderByDateAscShiftAsc(startDate, endDate);
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getByStudentEmail(String email) {
        List<Schedule> schedules = scheduleRepository.findByStudentEmailOrderByDateDesc(email);
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    @Transactional
    public ScheduleDTO update(Long id, ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));

        if (isBlank(request.getStudentName())) {
            String studentName = resolveStudentName(
                    schedule.getStudentName(),
                    request.getStudentEmail()
            );
            request.setStudentName(studentName);
        }
        scheduleMapper.updateEntity(request, schedule);
        attachUserIfExists(schedule, request.getStudentEmail());
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    @Transactional
    public ScheduleDTO updateStatusForStudent(Long id, String studentEmail, String status) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));

        User user = userRepository
                .findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", studentEmail));
        boolean isStudent = user.getRole() == User.Role.USER;
        boolean isOwner = studentEmail != null && studentEmail.equalsIgnoreCase(schedule.getStudentEmail());
        if (!isStudent || !isOwner) {
            throw new UnauthorizedException("You can only update your own schedule status");
        }

        Schedule.ScheduleStatus current = schedule.getStatus();
        Schedule.ScheduleStatus next;
        try {
            next = Schedule.ScheduleStatus.valueOf(status);
        } catch (IllegalArgumentException ex) {
            throw new ApiException("Invalid status", HttpStatus.BAD_REQUEST, "INVALID_STATUS");
        }

        if (current == Schedule.ScheduleStatus.DONE) {
            throw new ApiException("Schedule is already done", HttpStatus.BAD_REQUEST, "STATUS_LOCKED");
        }

        if (current == next) {
            return scheduleMapper.toDTO(schedule);
        }

        if (!isNextStatus(current, next)) {
            throw new ApiException("Invalid status transition", HttpStatus.BAD_REQUEST, "INVALID_TRANSITION");
        }

        schedule.setStatus(next);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule", "id", id);
        }
        scheduleRepository.deleteById(id);
    }

    private String resolveStudentName(String existingName, String email) {
        if (!isBlank(existingName)) {
            return existingName.trim();
        }
        if (isBlank(email)) {
            throw new ResourceNotFoundException("User", "email", null);
        }
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return user.getName();
    }

    private void attachUserIfExists(Schedule schedule, String email) {
        if (isBlank(email)) {
            return;
        }
        userRepository.findByEmail(email).ifPresent(schedule::setUser);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isNextStatus(Schedule.ScheduleStatus current, Schedule.ScheduleStatus next) {
        return (current == Schedule.ScheduleStatus.TODO && next == Schedule.ScheduleStatus.IN_PROGRESS)
                || (current == Schedule.ScheduleStatus.IN_PROGRESS && next == Schedule.ScheduleStatus.DONE);
    }
}
