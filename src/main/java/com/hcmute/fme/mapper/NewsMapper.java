package com.hcmute.fme.mapper;

import com.hcmute.fme.dto.request.NewsRequest;
import com.hcmute.fme.dto.response.NewsDTO;
import com.hcmute.fme.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsDTO toDTO(News news);

    List<NewsDTO> toDTOList(List<News> newsList);

    News toEntity(NewsRequest request);

    void updateEntity(NewsRequest request, @MappingTarget News news);
}
