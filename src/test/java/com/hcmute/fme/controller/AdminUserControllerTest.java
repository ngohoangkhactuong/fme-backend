package com.hcmute.fme.controller;

import com.hcmute.fme.dto.response.UserDTO;
import com.hcmute.fme.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getAll_returnsOk() throws Exception {
        when(userService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    void deactivate_returnsOk() throws Exception {
        mockMvc.perform(put("/admin/users/1/deactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void activate_returnsOk() throws Exception {
        mockMvc.perform(put("/admin/users/1/activate"))
                .andExpect(status().isOk());
    }
}
