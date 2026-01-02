package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.BannerRequest;
import com.hcmute.fme.dto.response.BannerDTO;
import com.hcmute.fme.entity.Banner;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.BannerMapper;
import com.hcmute.fme.repository.BannerRepository;
import com.hcmute.fme.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    @Transactional
    public BannerDTO create(BannerRequest request) {
        Banner banner = bannerMapper.toEntity(request);
        if (banner.getDisplayOrder() == null) {
            banner.setDisplayOrder(0);
        }
        if (banner.getIsActive() == null) {
            banner.setIsActive(true);
        }
        banner = bannerRepository.save(banner);
        return bannerMapper.toDTO(banner);
    }

    @Override
    @Transactional(readOnly = true)
    public BannerDTO getById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner", "id", id));
        return bannerMapper.toDTO(banner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BannerDTO> getAll() {
        List<Banner> banners = bannerRepository.findAllByOrderByDisplayOrderAsc();
        return bannerMapper.toDTOList(banners);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BannerDTO> getActive() {
        List<Banner> banners = bannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        return bannerMapper.toDTOList(banners);
    }

    @Override
    @Transactional
    public BannerDTO update(Long id, BannerRequest request) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner", "id", id));

        bannerMapper.updateEntity(request, banner);
        banner = bannerRepository.save(banner);
        return bannerMapper.toDTO(banner);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Banner", "id", id);
        }
        bannerRepository.deleteById(id);
    }
}
