package com.hcmute.fme.mapper;

import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleDTO toDTO(Schedule schedule);

    List<ScheduleDTO> toDTOList(List<Schedule> schedules);

    Schedule toEntity(ScheduleRequest request);

    void updateEntity(ScheduleRequest request, @MappingTarget Schedule schedule);
}
