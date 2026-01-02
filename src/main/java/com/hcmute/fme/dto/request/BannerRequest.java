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
public class BannerRequest {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private Integer displayOrder;

    private Boolean isActive;

    private String title;

    private String link;
}
