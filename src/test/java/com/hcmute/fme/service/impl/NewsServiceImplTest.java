package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.NewsRequest;
import com.hcmute.fme.dto.response.NewsDTO;
import com.hcmute.fme.entity.News;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.NewsMapper;
import com.hcmute.fme.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    void create_setsDefaultTrending() {
        NewsRequest request = NewsRequest.builder().build();
        News news = News.builder().build();

        when(newsMapper.toEntity(request)).thenReturn(news);
        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(newsMapper.toDTO(news)).thenReturn(new NewsDTO());

        newsService.create(request);

        ArgumentCaptor<News> captor = ArgumentCaptor.forClass(News.class);
        verify(newsRepository).save(captor.capture());
        assertFalse(Boolean.TRUE.equals(captor.getValue().getIsTrending()));
    }

    @Test
    void update_throwsWhenMissing() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                newsService.update(1L, NewsRequest.builder().build()));
    }

    @Test
    void delete_throwsWhenMissing() {
        when(newsRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> newsService.delete(1L));
    }
}
