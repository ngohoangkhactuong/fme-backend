package com.hcmute.fme.mapper;

import com.hcmute.fme.dto.request.DutyReportRequest;
import com.hcmute.fme.dto.response.DutyReportDTO;
import com.hcmute.fme.entity.DutyReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DutyReportMapper {

    @Mapping(target = "scheduleId", source = "schedule.id")
    @Mapping(target = "status", source = "status")
    DutyReportDTO toDTO(DutyReport dutyReport);

    List<DutyReportDTO> toDTOList(List<DutyReport> dutyReports);

    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "id", ignore = true)
    DutyReport toEntity(DutyReportRequest request);

    @Mapping(target = "schedule", ignore = true)
    void updateEntity(DutyReportRequest request, @MappingTarget DutyReport dutyReport);
}
