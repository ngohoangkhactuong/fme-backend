package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.NewsRequest;
import com.hcmute.fme.dto.response.NewsDTO;
import com.hcmute.fme.entity.News;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.NewsMapper;
import com.hcmute.fme.repository.NewsRepository;
import com.hcmute.fme.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    @Transactional
    public NewsDTO create(NewsRequest request) {
        News news = newsMapper.toEntity(request);
        if (news.getIsTrending() == null) {
            news.setIsTrending(false);
        }
        news = newsRepository.save(news);
        return newsMapper.toDTO(news);
    }

    @Override
    public NewsDTO getById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));
        return newsMapper.toDTO(news);
    }

    @Override
    public List<NewsDTO> getAll() {
        List<News> newsList = newsRepository.findAllByOrderByPublishedDateDesc();
        return newsMapper.toDTOList(newsList);
    }

    @Override
    public List<NewsDTO> getByCategory(String category) {
        List<News> newsList = newsRepository.findByCategoryOrderByPublishedDateDesc(category);
        return newsMapper.toDTOList(newsList);
    }

    @Override
    public List<NewsDTO> getTrending() {
        List<News> newsList = newsRepository.findByIsTrendingTrueOrderByPublishedDateDesc();
        return newsMapper.toDTOList(newsList);
    }

    @Override
    @Transactional
    public NewsDTO update(Long id, NewsRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));

        newsMapper.updateEntity(request, news);
        news = newsRepository.save(news);
        return newsMapper.toDTO(news);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new ResourceNotFoundException("News", "id", id);
        }
        newsRepository.deleteById(id);
    }
}
