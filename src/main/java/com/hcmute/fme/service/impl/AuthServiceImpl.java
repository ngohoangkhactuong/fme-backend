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
import com.hcmute.fme.mapper.UserMapper;
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
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(\\d+)@student\\.hcmute\\.edu\\.vn$", Pattern.CASE_INSENSITIVE);
    private static final String ADMIN_STUDENT_ID = "23146053";

    @Override
    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        String studentId = extractStudentId(request.getEmail());
        ensureEmailIsAvailable(request.getEmail());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .studentId(studentId)
                .role(resolveRole(studentId))
                .isActive(true)
                .build();

        user = userRepository.save(user);
        TokenPair tokenPair = issueTokens(user.getEmail());

        return buildAuthResponse(user, tokenPair);
    }

    @Override
    public AuthResponse signIn(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = findUserByEmail(request.getEmail());
        TokenPair tokenPair = new TokenPair(
                jwtTokenProvider.generateAccessToken(authentication),
                jwtTokenProvider.generateRefreshToken(user.getEmail())
        );

        return buildAuthResponse(user, tokenPair);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        User user = findUserByEmail(email);
        TokenPair tokenPair = issueTokens(email);

        return buildAuthResponse(user, tokenPair);
    }

    @Override
    public UserDTO getCurrentUser(String email) {
        return userMapper.toDTO(findUserByEmail(email));
    }

    @Override
    @Transactional
    public UserDTO updateProfile(String email, UpdateProfileRequest request) {
        User user = findUserByEmail(email);

        if (StringUtils.hasText(request.getName())) {
            user.setName(request.getName());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findUserByEmail(email);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private AuthResponse buildAuthResponse(User user, TokenPair tokenPair) {
        return AuthResponse.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
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

    private String extractStudentId(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new UnauthorizedException("Email must be in format: studentId@student.hcmute.edu.vn");
        }
        return matcher.group(1);
    }

    private void ensureEmailIsAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("User", "email", email);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private TokenPair issueTokens(String email) {
        return new TokenPair(
                jwtTokenProvider.generateAccessToken(email),
                jwtTokenProvider.generateRefreshToken(email)
        );
    }

    private User.Role resolveRole(String studentId) {
        return ADMIN_STUDENT_ID.equals(studentId) ? User.Role.ADMIN : User.Role.USER;
    }

    private record TokenPair(String accessToken, String refreshToken) {
    }
}
