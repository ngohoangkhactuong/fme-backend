package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.exception.UnauthorizedException;
import com.hcmute.fme.mapper.ScheduleMapper;
import com.hcmute.fme.entity.User;
import com.hcmute.fme.repository.ScheduleRepository;
import com.hcmute.fme.repository.UserRepository;
import com.hcmute.fme.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        schedule.setIsConfirmed(false);
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
    public ScheduleDTO confirm(Long id, String confirmedBy) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));

        User user = userRepository
                .findByEmail(confirmedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", confirmedBy));
        boolean isAdmin = user.getRole() == User.Role.ADMIN;
        boolean isOwner = confirmedBy != null && confirmedBy.equalsIgnoreCase(schedule.getStudentEmail());
        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You can only confirm your own schedule");
        }

        if (Boolean.TRUE.equals(schedule.getIsConfirmed())) {
            schedule.setIsConfirmed(false);
            schedule.setConfirmedBy(null);
            schedule.setConfirmedAt(null);
        } else {
            schedule.setIsConfirmed(true);
            schedule.setConfirmedBy(confirmedBy);
            schedule.setConfirmedAt(LocalDateTime.now());
        }
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
}
