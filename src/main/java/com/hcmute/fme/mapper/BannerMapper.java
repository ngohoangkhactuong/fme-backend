package com.hcmute.fme.mapper;

import com.hcmute.fme.dto.request.BannerRequest;
import com.hcmute.fme.dto.response.BannerDTO;
import com.hcmute.fme.entity.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BannerMapper {

    BannerDTO toDTO(Banner banner);

    List<BannerDTO> toDTOList(List<Banner> banners);

    Banner toEntity(BannerRequest request);

    void updateEntity(BannerRequest request, @MappingTarget Banner banner);
}
