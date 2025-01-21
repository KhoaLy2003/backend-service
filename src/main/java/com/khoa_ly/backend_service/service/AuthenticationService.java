package com.khoa_ly.backend_service.service;

import com.khoa_ly.backend_service.dto.request.LoginRequest;
import com.khoa_ly.backend_service.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    TokenResponse authenticate(LoginRequest loginRequest);
    TokenResponse refreshToken(HttpServletRequest request);
    void logout(HttpServletRequest request);
    default void revokeToken(String token) {
        // do nothing
    }
}
