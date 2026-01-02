package com.hcmute.fme.controller;

import com.hcmute.fme.dto.response.ApiResponse;
import com.hcmute.fme.dto.response.UserDTO;
import com.hcmute.fme.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - User Management", description = "Admin APIs for user management")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAll() {
        List<UserDTO> users = userService.getAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserDTO>> getById(@PathVariable Long id) {
        UserDTO user = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        userService.activate(id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", null));
    }
}
