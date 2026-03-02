package com.hcmute.fme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.fme.dto.request.ProgramRequest;
import com.hcmute.fme.dto.response.ProgramDTO;
import com.hcmute.fme.entity.Program;
import com.hcmute.fme.service.ProgramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProgramController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProgramService programService;

    @Test
    void create_returnsCreated() throws Exception {
        ProgramRequest request = ProgramRequest.builder().name("Prog").code("CS").type(Program.ProgramType.UNDERGRADUATE).build();
        when(programService.create(any(ProgramRequest.class))).thenReturn(new ProgramDTO());

        mockMvc.perform(post("/programs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(programService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/programs"))
                .andExpect(status().isOk());
    }
}
