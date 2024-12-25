package com.khoa_ly.backend_service.controller;

import com.khoa_ly.backend_service.dto.request.LoginRequest;
import com.khoa_ly.backend_service.dto.response.ResponseData;
import com.khoa_ly.backend_service.dto.response.TokenResponse;
import com.khoa_ly.backend_service.service.AuthenticationService;
import com.khoa_ly.backend_service.util.message.MessageLocalization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j(topic = "AuthenticateController")
@Tag(name = "Authenticate Controller")
public class AuthenticateController {
    private final AuthenticationService authenticationService;
    private final MessageLocalization messageLocalization;

    @GetMapping("/time")
    public ResponseData<String> getTime() {
        return new ResponseData<>(HttpStatus.OK.value(), "Time: " + new Date());
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate account", description = "API authenticate the account credentials")
    public ResponseData<TokenResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Authenticate account with email {} ", loginRequest.getEmail());
        return new ResponseData<>(HttpStatus.OK.value(), "Authenticate account successfully", authenticationService.authenticate(loginRequest));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "API logout user from the system by revoke the current refresh token")
    public ResponseData<String> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        return new ResponseData<>(HttpStatus.OK.value(), "Logout successfully");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "API to create new access token by refresh token")
    public ResponseData<TokenResponse> refresh(HttpServletRequest request) {
        return new ResponseData<>(HttpStatus.OK.value(), "Refresh successfully", authenticationService.refreshToken(request));
    }
}
