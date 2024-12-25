package com.khoa_ly.backend_service.service.impl;

import com.khoa_ly.backend_service.dto.request.LoginRequest;
import com.khoa_ly.backend_service.dto.response.TokenResponse;
import com.khoa_ly.backend_service.enumeration.AccountStatus;
import com.khoa_ly.backend_service.exception.AccountInactiveException;
import com.khoa_ly.backend_service.exception.NotFoundException;
import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.model.Token;
import com.khoa_ly.backend_service.repository.AccountRepository;
import com.khoa_ly.backend_service.repository.TokenRepository;
import com.khoa_ly.backend_service.service.AuthenticationService;
import com.khoa_ly.backend_service.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AuthenticateServiceImpl")
public class AuthenticateServiceImpl implements AuthenticationService {
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    public TokenResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticate user with email: {}", loginRequest.getEmail());
        Optional<Account> account = accountRepository.findByEmail(loginRequest.getEmail());

        if (account.isEmpty()) {
            log.error("Account not found for email: {}", loginRequest.getEmail());
            throw new NotFoundException("Account not found");
        } else {
            Account loginedAccount = account.get();

            if (loginedAccount.getStatus().equals(AccountStatus.INACTIVE)) {
                log.warn("Account for email {} is inactive.", loginRequest.getEmail());
                throw new AccountInactiveException("The account is inactive. Please activate your account.");
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword(),
                    loginedAccount.getAuthorities()
            ));

            String accessToken = jwtService.generateAccessToken(loginedAccount);
            String refreshToken = jwtService.generateRefreshToken(loginedAccount);

            Token existingToken = tokenRepository.findByAccountId(loginedAccount.getId()).orElse(null);
            if (existingToken != null) {
                existingToken.setRefreshToken(refreshToken);
                existingToken.setExpirationDate(jwtService.extractDate(accessToken));
                existingToken.setIsRevoked(false);
                tokenRepository.save(existingToken);
            } else {
                Token token = Token
                        .builder()
                        .account(loginedAccount)
                        .refreshToken(refreshToken)
                        .isRevoked(false)
                        .expirationDate(jwtService.extractDate(refreshToken))
                        .build();
                tokenRepository.save(token);
            }

            log.info("Authenticate successful for account with email: {}", loginRequest.getEmail());
            return TokenResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }

    @Override
    public TokenResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Invalid or missing authorization header.");
            throw new BadCredentialsException("Authorization header missing or invalid format");
        }
        refreshToken = authHeader.substring(7);
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        Account account = accountRepository.findByEmail(jwtService.extractUsername(existingToken.getRefreshToken())).orElseThrow(() -> new NotFoundException("Account not found"));
        boolean isValidToken = jwtService.isTokenValid(refreshToken, account) && !existingToken.getIsRevoked();
        if (isValidToken) {
            String newAccessToken = jwtService.generateAccessToken(account);
            String newRefreshToken = jwtService.generateRefreshToken(account);
            existingToken.setRefreshToken(newRefreshToken);
            existingToken.setExpirationDate(jwtService.extractDate(newRefreshToken));
            tokenRepository.save(existingToken);

            log.info("Generated new tokens for account: {}", account.getEmail());
            return TokenResponse
                    .builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String token;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Invalid or missing authorization header for logout.");
            return;
        }
        token = authHeader.substring(7);
        Token storedToken = tokenRepository.findByRefreshToken(token).orElse(null);
        if (storedToken != null) {
            storedToken.setIsRevoked(true);
            storedToken.setExpirationDate(null);
            tokenRepository.save(storedToken);
        }
    }
}
