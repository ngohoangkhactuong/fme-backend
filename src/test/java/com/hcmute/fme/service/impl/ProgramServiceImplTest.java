package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ProgramRequest;
import com.hcmute.fme.dto.response.ProgramDTO;
import com.hcmute.fme.entity.Program;
import com.hcmute.fme.exception.DuplicateResourceException;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.ProgramMapper;
import com.hcmute.fme.repository.ProgramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramServiceImplTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramMapper programMapper;

    @InjectMocks
    private ProgramServiceImpl programService;

    @Test
    void create_rejectsDuplicateCode() {
        ProgramRequest request = ProgramRequest.builder().code("CS").build();
        when(programRepository.existsByCode("CS")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> programService.create(request));
    }

    @Test
    void create_setsActive() {
        ProgramRequest request = ProgramRequest.builder().code("CS").build();
        Program program = Program.builder().code("CS").build();

        when(programRepository.existsByCode("CS")).thenReturn(false);
        when(programMapper.toEntity(request)).thenReturn(program);
        when(programRepository.save(any(Program.class))).thenReturn(program);
        when(programMapper.toDTO(program)).thenReturn(new ProgramDTO());

        programService.create(request);

        ArgumentCaptor<Program> captor = ArgumentCaptor.forClass(Program.class);
        verify(programRepository).save(captor.capture());
        assertTrue(Boolean.TRUE.equals(captor.getValue().getIsActive()));
    }

    @Test
    void update_rejectsCodeConflict() {
        ProgramRequest request = ProgramRequest.builder().code("CS2").build();
        Program program = Program.builder().id(1L).code("CS1").build();

        when(programRepository.findById(1L)).thenReturn(Optional.of(program));
        when(programRepository.existsByCode("CS2")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> programService.update(1L, request));
    }

    @Test
    void delete_softDeletes() {
        Program program = Program.builder().id(1L).isActive(true).build();

        when(programRepository.findById(1L)).thenReturn(Optional.of(program));
        when(programRepository.save(any(Program.class))).thenReturn(program);

        programService.delete(1L);

        assertFalse(Boolean.TRUE.equals(program.getIsActive()));
    }

    @Test
    void delete_throwsWhenMissing() {
        when(programRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> programService.delete(1L));
    }
}
