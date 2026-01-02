package com.hcmute.fme.service;

import com.hcmute.fme.dto.request.NewsRequest;
import com.hcmute.fme.dto.response.NewsDTO;

import java.util.List;

public interface NewsService {

    NewsDTO create(NewsRequest request);

    NewsDTO getById(Long id);

    List<NewsDTO> getAll();

    List<NewsDTO> getByCategory(String category);

    List<NewsDTO> getTrending();

    NewsDTO update(Long id, NewsRequest request);

    void delete(Long id);
}
