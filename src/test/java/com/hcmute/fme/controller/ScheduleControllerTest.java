package com.hcmute.fme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.fme.dto.request.ScheduleRequest;
import com.hcmute.fme.dto.response.ScheduleDTO;
import com.hcmute.fme.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void create_returnsCreated() throws Exception {
        ScheduleRequest request = ScheduleRequest.builder()
                .date(LocalDate.now())
                .shift("MORNING")
                .studentEmail("20190001@student.hcmute.edu.vn")
                .build();
        when(scheduleService.create(any(ScheduleRequest.class))).thenReturn(new ScheduleDTO());

        mockMvc.perform(post("/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(scheduleService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/schedules"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "20190001@student.hcmute.edu.vn")
    void confirm_returnsOk() throws Exception {
        when(scheduleService.confirm(eq(1L), any())).thenReturn(new ScheduleDTO());

        mockMvc.perform(put("/schedules/1/confirm"))
                .andExpect(status().isOk());
    }
}
