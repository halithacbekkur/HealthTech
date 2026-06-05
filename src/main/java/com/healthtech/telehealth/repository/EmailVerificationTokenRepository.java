package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByTokenAndUsedFalse(String token);
    void deleteByUserId(Long userId);
}
