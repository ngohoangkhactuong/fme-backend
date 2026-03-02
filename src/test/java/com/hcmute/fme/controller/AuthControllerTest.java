package com.hcmute.fme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.fme.dto.request.SignInRequest;
import com.hcmute.fme.dto.request.SignUpRequest;
import com.hcmute.fme.dto.response.AuthResponse;
import com.hcmute.fme.dto.response.UserDTO;
import com.hcmute.fme.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void signUp_returnsCreated() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("Test")
                .email("20190001@student.hcmute.edu.vn")
                .password("password123")
                .build();
        when(authService.signUp(any(SignUpRequest.class))).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").build());

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void signIn_returnsOk() throws Exception {
        SignInRequest request = SignInRequest.builder()
                .email("20190001@student.hcmute.edu.vn")
                .password("password123")
                .build();
        when(authService.signIn(any(SignInRequest.class))).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").build());

        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "20190001@student.hcmute.edu.vn")
    void getCurrentUser_returnsOk() throws Exception {
        when(authService.getCurrentUser("20190001@student.hcmute.edu.vn"))
                .thenReturn(UserDTO.builder().id(1L).email("20190001@student.hcmute.edu.vn").build());

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk());
    }
}
