package com.hcmute.fme.service;

import com.hcmute.fme.dto.request.ProgramRequest;
import com.hcmute.fme.dto.response.ProgramDTO;
import com.hcmute.fme.entity.Program;

import java.util.List;

public interface ProgramService {

    ProgramDTO create(ProgramRequest request);

    ProgramDTO getById(Long id);

    ProgramDTO getByCode(String code);

    List<ProgramDTO> getAll();

    List<ProgramDTO> getByType(Program.ProgramType type);

    ProgramDTO update(Long id, ProgramRequest request);

    void delete(Long id);
}
