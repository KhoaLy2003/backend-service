package com.khoa_ly.backend_service.repository;

import com.khoa_ly.backend_service.enumeration.AccountStatus;
import com.khoa_ly.backend_service.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    Page<Account> findByStatus(AccountStatus status, Pageable pageable);
    Optional<Account> findByEmail(String email);
}
