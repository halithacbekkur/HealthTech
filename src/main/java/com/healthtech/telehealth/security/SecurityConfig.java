package com.healthtech.telehealth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Bu annotation (aciklama) @PreAuthorize kullanimini aktif eder
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF korumasini kapat (REST API'lerde gerekli degil)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session kullanma, her istekte JWT kontrol et
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Giris ve kayit herkese acik
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger herkese acik
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/*/approve").hasRole("DOCTOR") // Sadece doktor onaylayabilir
                        .anyRequest().authenticated() // Diger tum istekler icin giris yapmis olmak gerekli
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT filtresini Spring Security'nin kendi filtresinden once calistir

        return http.build();
    }
}