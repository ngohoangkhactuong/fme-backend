package com.hcmute.fme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.fme.dto.request.NewsRequest;
import com.hcmute.fme.dto.response.NewsDTO;
import com.hcmute.fme.service.NewsService;
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

@WebMvcTest(NewsController.class)
@AutoConfigureMockMvc(addFilters = false)
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsService newsService;

    @Test
    void create_returnsCreated() throws Exception {
        NewsRequest request = NewsRequest.builder().title("Title").category("Cat").build();
        when(newsService.create(any(NewsRequest.class))).thenReturn(new NewsDTO());

        mockMvc.perform(post("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(newsService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/news"))
                .andExpect(status().isOk());
    }
}
