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

    ScheduleDTO updateStatusForStudent(Long id, String studentEmail, String status);

    void delete(Long id);
}
