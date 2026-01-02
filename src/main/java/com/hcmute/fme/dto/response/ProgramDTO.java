package com.hcmute.fme.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String type;
    private Boolean isActive;
}
