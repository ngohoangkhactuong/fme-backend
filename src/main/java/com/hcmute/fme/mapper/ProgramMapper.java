package com.hcmute.fme.mapper;

import com.hcmute.fme.dto.request.ProgramRequest;
import com.hcmute.fme.dto.response.ProgramDTO;
import com.hcmute.fme.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramMapper {

    ProgramDTO toDTO(Program program);

    List<ProgramDTO> toDTOList(List<Program> programs);

    Program toEntity(ProgramRequest request);

    void updateEntity(ProgramRequest request, @MappingTarget Program program);
}
