package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.BannerRequest;
import com.hcmute.fme.dto.response.BannerDTO;
import com.hcmute.fme.entity.Banner;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.mapper.BannerMapper;
import com.hcmute.fme.repository.BannerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BannerServiceImplTest {

    @Mock
    private BannerRepository bannerRepository;

    @Mock
    private BannerMapper bannerMapper;

    @InjectMocks
    private BannerServiceImpl bannerService;

    @Test
    void create_setsDefaults() {
        BannerRequest request = BannerRequest.builder().build();
        Banner banner = Banner.builder().build();

        when(bannerMapper.toEntity(request)).thenReturn(banner);
        when(bannerRepository.save(any(Banner.class))).thenReturn(banner);
        when(bannerMapper.toDTO(banner)).thenReturn(new BannerDTO());

        bannerService.create(request);

        ArgumentCaptor<Banner> captor = ArgumentCaptor.forClass(Banner.class);
        verify(bannerRepository).save(captor.capture());
        assertEquals(0, captor.getValue().getDisplayOrder());
        assertTrue(Boolean.TRUE.equals(captor.getValue().getIsActive()));
    }

    @Test
    void update_throwsWhenMissing() {
        when(bannerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bannerService.update(1L, BannerRequest.builder().build()));
    }

    @Test
    void delete_throwsWhenMissing() {
        when(bannerRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bannerService.delete(1L));
    }
}
