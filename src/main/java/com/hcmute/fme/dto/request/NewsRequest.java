package com.hcmute.fme.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Published date is required")
    private String publishedDate;

    private String imageUrl;

    @NotBlank(message = "URL is required")
    private String url;

    private Boolean isTrending;

    private String content;
}
