package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.UserResponseDTO;
import com.healthtech.telehealth.entity.Role;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UserService için Mockito tabanlı birim testleri.
 *
 * @author Ahmet Akif Yılmaz
 * @sprint Hafta 6 - Final Test
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setFullName("Akif Yılmaz");
        sampleUser.setEmail("akif@test.com");
        sampleUser.setPhone("05551112233");
        sampleUser.setRole(Role.PATIENT);
        sampleUser.setPassword("hashedPassword");
    }

    @Test
    @DisplayName("getAllUsers tüm kullanıcıları DTO olarak döndürmeli")
    void getAllUsers_shouldReturnUserDTOList() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setFullName("Test Kullanıcı");
        user2.setEmail("test@test.com");
        user2.setRole(Role.DOCTOR);

        when(userRepository.findAll()).thenReturn(Arrays.asList(sampleUser, user2));

        // When
        List<UserResponseDTO> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("akif@test.com", result.get(0).getEmail());
        assertEquals(Role.DOCTOR, result.get(1).getRole());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getUserById var olan kullanıcıyı bulmalı")
    void getUserById_shouldReturnUser_whenExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        // When
        UserResponseDTO result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("akif@test.com", result.getEmail());
        assertEquals(Role.PATIENT, result.getRole());
    }

    @Test
    @DisplayName("getUserById olmayan kullanıcı için UserNotFoundException atmalı")
    void getUserById_shouldThrowException_whenNotExists() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(99L));
        assertEquals("Kullanıcı bulunamadı", ex.getMessage());
    }

    @Test
    @DisplayName("BUG-007 TEST: createUser şifreyi hash'lememeli (mevcut bug)")
    void createUser_shouldHashPassword_butCurrentlyDoesNot() {
        // Given
        User newUser = new User();
        newUser.setEmail("yeni@test.com");
        newUser.setPassword("plainPassword123");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        User result = userService.createUser(newUser);

        // Then - Bu test MEVCUT KODA GÖRE GEÇER ama YANLIŞ DAVRANIŞI gösterir
        assertEquals("plainPassword123", result.getPassword(),
                "BUG: Şifre hash'lenmiyor - plain text kaydediliyor!");

        // passwordEncoder.encode() ÇAĞRILMAMIŞ olmalı (bug bu)
        verify(passwordEncoder, never()).encode(anyString());

        // Bu testin mantığı: kod düzeltilince bu test FAIL olmalı,
        // o zaman testi şuna çevirmeli:
        // verify(passwordEncoder, times(1)).encode("plainPassword123");
    }

    @Test
    @DisplayName("deleteUser var olan kullanıcıyı silmeli")
    void deleteUser_shouldDelete_whenExists() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUser olmayan kullanıcı için exception atmalı")
    void deleteUser_shouldThrowException_whenNotExists() {
        // Given
        when(userRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(99L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("updateUser şifre boşsa eski şifreyi korumalı")
    void updateUser_shouldKeepOldPassword_whenNewPasswordIsEmpty() {
        // Given
        User updateRequest = new User();
        updateRequest.setFullName("Güncel İsim");
        updateRequest.setEmail("akif@test.com");
        updateRequest.setPassword(""); // boş şifre

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        // When
        userService.updateUser(1L, updateRequest);

        // Then
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("getCurrentUser email'e göre kullanıcı dönmeli")
    void getCurrentUser_shouldReturnUser_byEmail() {
        // Given
        when(userRepository.findByEmail("akif@test.com")).thenReturn(Optional.of(sampleUser));

        // When
        UserResponseDTO result = userService.getCurrentUser("akif@test.com");

        // Then
        assertNotNull(result);
        assertEquals(sampleUser.getId(), result.getId());
        assertEquals(sampleUser.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("getCurrentUser olmayan email için exception atmalı")
    void getCurrentUser_shouldThrowException_whenEmailNotFound() {
        // Given
        when(userRepository.findByEmail("yok@test.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class,
                () -> userService.getCurrentUser("yok@test.com"));
    }
}
