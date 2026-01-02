package com.hcmute.fme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {

    private Long id;
    private String imageUrl;
    private Integer displayOrder;
    private Boolean isActive;
    private String title;
    private String link;
}
