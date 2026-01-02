package com.hcmute.fme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    private Long id;
    private String title;
    private String category;
    private String publishedDate;
    private String imageUrl;
    private String url;
    private Boolean isTrending;
    private String content;
}
