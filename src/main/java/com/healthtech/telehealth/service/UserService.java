package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.UserResponseDTO;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole()
                ))
                .toList();
    }

    // BUG-007 FIX: createUser sifreleme yok — artik hash'leniyor
    public UserResponseDTO createUser(User user) {
        // Sifreyi hashle (register gibi)
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: ID " + id));
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Kullanıcı bulunamadı: ID " + id);
        }
        userRepository.deleteById(id);
    }

    // BUG-008 FIX: Rol bilgisi korunuyor, null gelmez
    public UserResponseDTO updateUser(Long id, User userRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: ID " + id));

        // Temel bilgileri guncelle
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        // Sifre gelirse hashle, gelmezse mevcut sifre korunsun
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        // BUG-008 FIX: Rol alanı — request'te rol yoksa mevcut rol korunsun
        // Rol degisikligi sadece ADMIN tarafindan yapilmali (gelecek gelistirme)
        // Simdilik mevcut rol her zaman korunuyor

        User updatedUser = userRepository.save(user);

        return new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getFullName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getRole()
        );
    }

    public UserResponseDTO getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + email));

        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}