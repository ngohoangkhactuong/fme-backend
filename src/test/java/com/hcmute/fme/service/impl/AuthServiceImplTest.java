package com.hcmute.fme.service.impl;

import com.hcmute.fme.dto.request.ChangePasswordRequest;
import com.hcmute.fme.dto.request.GoogleSignInRequest;
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
import com.hcmute.fme.security.GoogleTokenVerifier;
import com.hcmute.fme.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @Mock
    private GoogleTokenVerifier googleTokenVerifier;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("20190001@student.hcmute.edu.vn")
                .password("encoded")
                .studentId("20190001")
                .role(User.Role.USER)
                .isActive(true)
                .build();
    }

    @Test
    void signUp_createsUserAndReturnsTokens() {
        SignUpRequest request = SignUpRequest.builder()
                .name("Test User")
                .email("20190001@student.hcmute.edu.vn")
                .password("password123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateAccessToken(anyString())).thenReturn("access");
        when(jwtTokenProvider.generateRefreshToken(anyString())).thenReturn("refresh");

        AuthResponse response = authService.signUp(request);

        assertNotNull(response);
        assertEquals("access", response.getAccessToken());
        assertEquals("refresh", response.getRefreshToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signUp_rejectsDuplicateEmail() {
        SignUpRequest request = SignUpRequest.builder()
                .name("Test User")
                .email("20190001@student.hcmute.edu.vn")
                .password("password123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.signUp(request));
    }

    @Test
    void signUp_rejectsInvalidEmailFormat() {
        SignUpRequest request = SignUpRequest.builder()
                .name("Test User")
                .email("user@example.com")
                .password("password123")
                .build();

        assertThrows(UnauthorizedException.class, () -> authService.signUp(request));
    }

    @Test
    void signIn_authenticatesAndReturnsTokens() {
        SignInRequest request = SignInRequest.builder()
                .email(user.getEmail())
                .password("password123")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), "password123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access");
        when(jwtTokenProvider.generateRefreshToken(user.getEmail())).thenReturn("refresh");

        AuthResponse response = authService.signIn(request);

        assertEquals("access", response.getAccessToken());
        assertEquals("refresh", response.getRefreshToken());
    }

    @Test
    void signInWithGoogle_createsNewUserWhenMissing() {
        GoogleSignInRequest request = GoogleSignInRequest.builder()
                .idToken("token")
                .build();
        GoogleTokenVerifier.GoogleUserInfo info = new GoogleTokenVerifier.GoogleUserInfo(
                "20190001@student.hcmute.edu.vn",
                "Test User",
                "avatar"
        );

        when(googleTokenVerifier.verify(request.getIdToken())).thenReturn(info);
        when(userRepository.findByEmail(info.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateAccessToken(anyString())).thenReturn("access");
        when(jwtTokenProvider.generateRefreshToken(anyString())).thenReturn("refresh");

        AuthResponse response = authService.signInWithGoogle(request);

        assertEquals("access", response.getAccessToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signInWithGoogle_rejectsDeactivatedUser() {
        GoogleSignInRequest request = GoogleSignInRequest.builder()
                .idToken("token")
                .build();
        GoogleTokenVerifier.GoogleUserInfo info = new GoogleTokenVerifier.GoogleUserInfo(
                "20190001@student.hcmute.edu.vn",
                "Test User",
                null
        );
        User inactiveUser = User.builder()
                .email(info.email())
                .name(info.name())
                .password("encoded")
                .studentId("20190001")
                .role(User.Role.USER)
                .isActive(false)
                .build();

        when(googleTokenVerifier.verify(request.getIdToken())).thenReturn(info);
        when(userRepository.findByEmail(info.email())).thenReturn(Optional.of(inactiveUser));

        assertThrows(UnauthorizedException.class, () -> authService.signInWithGoogle(request));
    }

    @Test
    void refreshToken_rejectsInvalidToken() {
        when(jwtTokenProvider.validateToken("bad")).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> authService.refreshToken("bad"));
    }

    @Test
    void updateProfile_updatesNameAndAvatar() {
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .name("New Name")
                .avatar("avatar")
                .build();
        UserDTO dto = UserDTO.builder().id(1L).email(user.getEmail()).name("New Name").build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(dto);

        UserDTO response = authService.updateProfile(user.getEmail(), request);

        assertEquals("New Name", response.getName());
    }

    @Test
    void changePassword_rejectsWrongPassword() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("wrong")
                .newPassword("new")
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.changePassword(user.getEmail(), request));
    }

    @Test
    void changePassword_updatesPassword() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("old")
                .newPassword("new")
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("encoded-new");

        authService.changePassword(user.getEmail(), request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("encoded-new", captor.getValue().getPassword());
    }

    @Test
    void getCurrentUser_returnsUserDTO() {
        UserDTO dto = UserDTO.builder().id(1L).email(user.getEmail()).name(user.getName()).build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO response = authService.getCurrentUser(user.getEmail());

        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    void getCurrentUser_throwsWhenMissing() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.getCurrentUser(user.getEmail()));
    }
}
