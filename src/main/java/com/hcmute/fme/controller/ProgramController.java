package com.hcmute.fme.controller;

import com.hcmute.fme.dto.request.ProgramRequest;
import com.hcmute.fme.dto.response.ApiResponse;
import com.hcmute.fme.dto.response.ProgramDTO;
import com.hcmute.fme.entity.Program;
import com.hcmute.fme.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programs")
@RequiredArgsConstructor
@Tag(name = "Program", description = "Academic program management APIs")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping
    @Operation(summary = "Create program (Admin only)")
    public ResponseEntity<ApiResponse<ProgramDTO>> create(@Valid @RequestBody ProgramRequest request) {
        ProgramDTO program = programService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Program created successfully", program));
    }

    @GetMapping
    @Operation(summary = "Get all programs")
    public ResponseEntity<ApiResponse<List<ProgramDTO>>> getAll() {
        List<ProgramDTO> programs = programService.getAll();
        return ResponseEntity.ok(ApiResponse.success(programs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get program by ID")
    public ResponseEntity<ApiResponse<ProgramDTO>> getById(@PathVariable Long id) {
        ProgramDTO program = programService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(program));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get program by code")
    public ResponseEntity<ApiResponse<ProgramDTO>> getByCode(@PathVariable String code) {
        ProgramDTO program = programService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(program));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get programs by type")
    public ResponseEntity<ApiResponse<List<ProgramDTO>>> getByType(@PathVariable Program.ProgramType type) {
        List<ProgramDTO> programs = programService.getByType(type);
        return ResponseEntity.ok(ApiResponse.success(programs));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update program (Admin only)")
    public ResponseEntity<ApiResponse<ProgramDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProgramRequest request) {
        ProgramDTO program = programService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Program updated successfully", program));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete program (Admin only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        programService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Program deleted successfully", null));
    }
}
