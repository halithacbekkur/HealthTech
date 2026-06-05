package com.healthtech.telehealth.config;

import com.healthtech.telehealth.entity.Role;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Uygulama ilk başlatıldığında varsayılan admin hesabı oluşturur.
 * Email: admin@telehealth.com
 * Şifre: admin123
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@telehealth.com").isEmpty()) {
                User admin = new User();
                admin.setFullName("Sistem Yöneticisi");
                admin.setEmail("admin@telehealth.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setPhone("0000000000");
                userRepository.save(admin);
                System.out.println("✅ Varsayılan admin hesabı oluşturuldu: admin@telehealth.com / admin123");
            }
        };
    }
}
