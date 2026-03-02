package com.hcmute.fme.service.impl;

import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.repository.ProgramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgramServiceImplAdditionalTest {

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private ProgramServiceImpl programService;

    @Test
    void getById_throwsWhenMissing() {
        when(programRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> programService.getById(1L));
    }

    @Test
    void getByCode_throwsWhenMissing() {
        when(programRepository.findByCode("CS")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> programService.getByCode("CS"));
    }
}
