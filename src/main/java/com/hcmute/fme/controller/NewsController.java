package com.hcmute.fme.controller;

import com.hcmute.fme.dto.request.NewsRequest;
import com.hcmute.fme.dto.response.ApiResponse;
import com.hcmute.fme.dto.response.NewsDTO;
import com.hcmute.fme.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "News management APIs")
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    @Operation(summary = "Create news article (Admin only)")
    public ResponseEntity<ApiResponse<NewsDTO>> create(@Valid @RequestBody NewsRequest request) {
        NewsDTO news = newsService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("News created successfully", news));
    }

    @GetMapping
    @Operation(summary = "Get all news articles")
    public ResponseEntity<ApiResponse<List<NewsDTO>>> getAll() {
        List<NewsDTO> newsList = newsService.getAll();
        return ResponseEntity.ok(ApiResponse.success(newsList));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get news by ID")
    public ResponseEntity<ApiResponse<NewsDTO>> getById(@PathVariable Long id) {
        NewsDTO news = newsService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(news));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get news by category")
    public ResponseEntity<ApiResponse<List<NewsDTO>>> getByCategory(@PathVariable String category) {
        List<NewsDTO> newsList = newsService.getByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(newsList));
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending news")
    public ResponseEntity<ApiResponse<List<NewsDTO>>> getTrending() {
        List<NewsDTO> newsList = newsService.getTrending();
        return ResponseEntity.ok(ApiResponse.success(newsList));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update news (Admin only)")
    public ResponseEntity<ApiResponse<NewsDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody NewsRequest request) {
        NewsDTO news = newsService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("News updated successfully", news));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete news (Admin only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        newsService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("News deleted successfully", null));
    }
}
