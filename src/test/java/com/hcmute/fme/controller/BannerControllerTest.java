package com.hcmute.fme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.fme.dto.request.BannerRequest;
import com.hcmute.fme.dto.response.BannerDTO;
import com.hcmute.fme.service.BannerService;
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

@WebMvcTest(BannerController.class)
@AutoConfigureMockMvc(addFilters = false)
class BannerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BannerService bannerService;

    @Test
    void create_returnsCreated() throws Exception {
        BannerRequest request = BannerRequest.builder().imageUrl("img").build();
        when(bannerService.create(any(BannerRequest.class))).thenReturn(new BannerDTO());

        mockMvc.perform(post("/banners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(bannerService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/banners"))
                .andExpect(status().isOk());
    }
}
