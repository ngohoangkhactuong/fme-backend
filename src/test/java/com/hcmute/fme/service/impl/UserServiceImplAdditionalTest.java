package com.hcmute.fme.service.impl;

import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplAdditionalTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getByEmail_throwsWhenMissing() {
        when(userRepository.findByEmail("missing@student.hcmute.edu.vn")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail("missing@student.hcmute.edu.vn"));
    }
}
