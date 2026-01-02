package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ProgramRequest;
import com.hcmute.fme.dto.response.ProgramDTO;
import com.hcmute.fme.entity.Program;
import com.hcmute.fme.exception.DuplicateResourceException;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.ProgramMapper;
import com.hcmute.fme.repository.ProgramRepository;
import com.hcmute.fme.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

    @Override
    @Transactional
    public ProgramDTO create(ProgramRequest request) {
        if (programRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Program", "code", request.getCode());
        }

        Program program = programMapper.toEntity(request);
        program.setIsActive(true);
        program = programRepository.save(program);
        return programMapper.toDTO(program);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramDTO getById(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program", "id", id));
        return programMapper.toDTO(program);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramDTO getByCode(String code) {
        Program program = programRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Program", "code", code));
        return programMapper.toDTO(program);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramDTO> getAll() {
        List<Program> programs = programRepository.findByIsActiveTrueOrderByTypeAscNameAsc();
        return programMapper.toDTOList(programs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramDTO> getByType(Program.ProgramType type) {
        List<Program> programs = programRepository.findByTypeAndIsActiveTrueOrderByNameAsc(type);
        return programMapper.toDTOList(programs);
    }

    @Override
    @Transactional
    public ProgramDTO update(Long id, ProgramRequest request) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program", "id", id));

        // Check if new code conflicts with existing
        if (!program.getCode().equals(request.getCode()) && programRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Program", "code", request.getCode());
        }

        programMapper.updateEntity(request, program);
        program = programRepository.save(program);
        return programMapper.toDTO(program);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program", "id", id));
        
        // Soft delete
        program.setIsActive(false);
        programRepository.save(program);
    }
}
