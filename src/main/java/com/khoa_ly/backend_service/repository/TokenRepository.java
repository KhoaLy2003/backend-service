package com.khoa_ly.backend_service.repository;

import com.khoa_ly.backend_service.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByRefreshToken(String refreshToken);
    Optional<Token> findByAccountId(Long accountId);
}
