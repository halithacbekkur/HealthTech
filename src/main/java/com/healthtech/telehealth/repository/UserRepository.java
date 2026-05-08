package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.Role;
import com.healthtech.telehealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Rol bazli kullanici listeleme (doktorlari listele, hastalari listele vb.)
    List<User> findByRole(Role role);

    // Email var mi kontrolu (duplicate email check)
    boolean existsByEmail(String email);
}