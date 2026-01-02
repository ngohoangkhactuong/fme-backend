package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ChangePasswordRequest;
import com.hcmute.fme.dto.request.SignInRequest;
import com.hcmute.fme.dto.request.SignUpRequest;
import com.hcmute.fme.dto.request.UpdateProfileRequest;
import com.hcmute.fme.dto.response.AuthResponse;
import com.hcmute.fme.dto.response.UserDTO;
import com.hcmute.fme.entity.User;
import com.hcmute.fme.exception.DuplicateResourceException;
import com.hcmute.fme.exception.ResourceNotFoundException;
import com.hcmute.fme.exception.UnauthorizedException;
import com.hcmute.fme.repository.UserRepository;
import com.hcmute.fme.security.JwtTokenProvider;
import com.hcmute.fme.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(\\d+)@student\\.hcmute\\.edu\\.vn$", Pattern.CASE_INSENSITIVE);
    private static final String ADMIN_STUDENT_ID = "23146053";

    @Override
    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        // Validate email format
        Matcher matcher = EMAIL_PATTERN.matcher(request.getEmail());
        if (!matcher.matches()) {
            throw new UnauthorizedException("Email must be in format: studentId@student.hcmute.edu.vn");
        }

        String studentId = matcher.group(1);

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        // Determine role based on student ID
        User.Role role = ADMIN_STUDENT_ID.equals(studentId) ? User.Role.ADMIN : User.Role.USER;

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .studentId(studentId)
                .role(role)
                .isActive(true)
                .build();

        user = userRepository.save(user);

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Override
    public AuthResponse signIn(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    @Override
    public UserDTO getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return toUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        user = userRepository.save(user);
        return toUserDTO(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpiration())
                .user(AuthResponse.UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .studentId(user.getStudentId())
                        .avatar(user.getAvatar())
                        .role(user.getRole().name().toLowerCase())
                        .build())
                .build();
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .studentId(user.getStudentId())
                .avatar(user.getAvatar())
                .role(user.getRole().name().toLowerCase())
                .isActive(user.getIsActive())
                .build();
    }
}
