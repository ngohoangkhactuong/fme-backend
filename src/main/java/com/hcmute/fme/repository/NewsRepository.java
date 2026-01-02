package com.hcmute.fme.repository;

import com.hcmute.fme.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    
    List<News> findByCategoryOrderByPublishedDateDesc(String category);
    
    List<News> findByIsTrendingTrueOrderByPublishedDateDesc();
    
    List<News> findAllByOrderByPublishedDateDesc();
}
