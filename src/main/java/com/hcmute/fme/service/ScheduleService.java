package com.hcmute.fme.service;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleDTO create(ScheduleRequest request);

    ScheduleDTO getById(Long id);

    List<ScheduleDTO> getAll();

    List<ScheduleDTO> getByDate(LocalDate date);

    List<ScheduleDTO> getByDateRange(LocalDate startDate, LocalDate endDate);

    List<ScheduleDTO> getByStudentEmail(String email);

    ScheduleDTO update(Long id, ScheduleRequest request);

    ScheduleDTO confirm(Long id, String confirmedBy);

    void delete(Long id);
}
