package com.hcmute.fme.service;

import com.hcmute.fme.dto.request.ChangePasswordRequest;
import com.hcmute.fme.dto.request.SignInRequest;
import com.hcmute.fme.dto.request.SignUpRequest;
import com.hcmute.fme.dto.request.UpdateProfileRequest;
import com.hcmute.fme.dto.response.AuthResponse;
import com.hcmute.fme.dto.response.UserDTO;

public interface AuthService {

    AuthResponse signUp(SignUpRequest request);

    AuthResponse signIn(SignInRequest request);

    AuthResponse refreshToken(String refreshToken);

    UserDTO getCurrentUser(String email);

    UserDTO updateProfile(String email, UpdateProfileRequest request);

    void changePassword(String email, ChangePasswordRequest request);
}
