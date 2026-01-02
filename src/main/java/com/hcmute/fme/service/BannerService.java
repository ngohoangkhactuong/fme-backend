package com.hcmute.fme.service;

import com.hcmute.fme.dto.request.BannerRequest;
import com.hcmute.fme.dto.response.BannerDTO;

import java.util.List;

public interface BannerService {

    BannerDTO create(BannerRequest request);

    BannerDTO getById(Long id);

    List<BannerDTO> getAll();

    List<BannerDTO> getActive();

    BannerDTO update(Long id, BannerRequest request);

    void delete(Long id);
}
