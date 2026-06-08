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
    private final com.healthtech.telehealth.repository.DoctorProfileRepository doctorProfileRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       com.healthtech.telehealth.repository.DoctorProfileRepository doctorProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.doctorProfileRepository = doctorProfileRepository;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
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
        return mapToDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: ID " + id));
        return mapToDTO(user);
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
        return mapToDTO(updatedUser);
    }

    public UserResponseDTO getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + email));

        return mapToDTO(user);
    }

    // G17: Doktor listeleme (hasta randevu alirken sadece onayli doktorlar secilebilsin)
    public List<UserResponseDTO> getDoctors() {
        return doctorProfileRepository.findByApprovalStatus(com.healthtech.telehealth.entity.DoctorApprovalStatus.APPROVED)
                .stream()
                .map(profile -> mapToDTO(profile.getUser()))
                .toList();
    }

    // G17: Rol bazli kullanici listeleme
    public List<UserResponseDTO> getUsersByRole(com.healthtech.telehealth.entity.Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Yardımcı: User → DTO (genişletilmiş alanlar)
    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setTcKimlik(user.getTcKimlik());
        dto.setBirthDate(user.getBirthDate());
        dto.setGender(user.getGender());
        dto.setProfilePhotoUrl(user.getProfilePhotoUrl());
        dto.setAccountStatus(user.getAccountStatus());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setPhoneVerified(user.isPhoneVerified());
        dto.setBloodGroup(user.getBloodGroup());
        dto.setChronicDiseases(user.getChronicDiseases());
        dto.setDisabilities(user.getDisabilities());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}