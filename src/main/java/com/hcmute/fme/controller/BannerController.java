package com.hcmute.fme.controller;

import com.hcmute.fme.dto.request.BannerRequest;
import com.hcmute.fme.dto.response.ApiResponse;
import com.hcmute.fme.dto.response.BannerDTO;
import com.hcmute.fme.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
@Tag(name = "Banner", description = "Banner management APIs")
public class BannerController {

    private final BannerService bannerService;

    @PostMapping
    @Operation(summary = "Create banner (Admin only)")
    public ResponseEntity<ApiResponse<BannerDTO>> create(@Valid @RequestBody BannerRequest request) {
        BannerDTO banner = bannerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Banner created successfully", banner));
    }

    @GetMapping
    @Operation(summary = "Get all banners")
    public ResponseEntity<ApiResponse<List<BannerDTO>>> getAll() {
        List<BannerDTO> banners = bannerService.getAll();
        return ResponseEntity.ok(ApiResponse.success(banners));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active banners")
    public ResponseEntity<ApiResponse<List<BannerDTO>>> getActive() {
        List<BannerDTO> banners = bannerService.getActive();
        return ResponseEntity.ok(ApiResponse.success(banners));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get banner by ID")
    public ResponseEntity<ApiResponse<BannerDTO>> getById(@PathVariable Long id) {
        BannerDTO banner = bannerService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(banner));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update banner (Admin only)")
    public ResponseEntity<ApiResponse<BannerDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody BannerRequest request) {
        BannerDTO banner = bannerService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Banner updated successfully", banner));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete banner (Admin only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Banner deleted successfully", null));
    }
}
