package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.entity.Schedule;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.ScheduleMapper;
import com.hcmute.fme.repository.ScheduleRepository;
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

    @Override
    @Transactional
    public ScheduleDTO create(ScheduleRequest request) {
        Schedule schedule = scheduleMapper.toEntity(request);
        schedule.setIsConfirmed(false);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    public ScheduleDTO getById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    public List<ScheduleDTO> getAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    public List<ScheduleDTO> getByDate(LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByDateOrderByShiftAsc(date);
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    public List<ScheduleDTO> getByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = scheduleRepository.findByDateBetweenOrderByDateAscShiftAsc(startDate, endDate);
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    public List<ScheduleDTO> getByStudentEmail(String email) {
        List<Schedule> schedules = scheduleRepository.findByStudentEmailOrderByDateDesc(email);
        return scheduleMapper.toDTOList(schedules);
    }

    @Override
    @Transactional
    public ScheduleDTO update(Long id, ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));

        scheduleMapper.updateEntity(request, schedule);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    @Transactional
    public ScheduleDTO confirm(Long id, String confirmedBy) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));

        schedule.setIsConfirmed(true);
        schedule.setConfirmedBy(confirmedBy);
        schedule.setConfirmedAt(LocalDateTime.now());
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
}
