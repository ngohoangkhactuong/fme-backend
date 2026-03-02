package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.response.UserDTO;
import com.hcmute.fme.entity.User;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.UserMapper;
import com.hcmute.fme.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getById_throwsWhenMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L));
    }

    @Test
    void deactivate_setsInactive() {
        User user = User.builder().id(1L).isActive(true).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivate(1L);

        assertFalse(Boolean.TRUE.equals(user.getIsActive()));
    }

    @Test
    void activate_setsActive() {
        User user = User.builder().id(1L).isActive(false).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.activate(1L);

        assertTrue(Boolean.TRUE.equals(user.getIsActive()));
    }

    @Test
    void getByEmail_returnsDTO() {
        User user = User.builder().id(1L).email("test@student.hcmute.edu.vn").build();
        UserDTO dto = UserDTO.builder().id(1L).email(user.getEmail()).build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO response = userService.getByEmail(user.getEmail());

        assertEquals(user.getEmail(), response.getEmail());
    }
}
