package com.hcmute.fme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.fme.dto.request.DutyReportDraftRequest;
import com.hcmute.fme.dto.request.DutyReportRequest;
import com.hcmute.fme.dto.response.DutyReportDTO;
import com.hcmute.fme.service.DutyReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DutyReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class DutyReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DutyReportService dutyReportService;

    @Test
    @WithMockUser(username = "20190001@student.hcmute.edu.vn")
    void create_returnsCreated() throws Exception {
        DutyReportRequest request = DutyReportRequest.builder()
                .scheduleId(1L)
                .title("Title")
                .report("Report")
                .build();
        when(dutyReportService.createForStudent(any(DutyReportRequest.class), any()))
                .thenReturn(new DutyReportDTO());

        mockMvc.perform(post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "20190001@student.hcmute.edu.vn")
    void saveDraft_returnsOk() throws Exception {
        DutyReportDraftRequest request = DutyReportDraftRequest.builder()
                .scheduleId(1L)
                .title("Draft")
                .build();
        when(dutyReportService.saveDraftForStudent(any(DutyReportDraftRequest.class), any()))
                .thenReturn(new DutyReportDTO());

        mockMvc.perform(post("/reports/draft")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(dutyReportService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk());
    }
}
