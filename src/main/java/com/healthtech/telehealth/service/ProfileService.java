package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Profil yönetim servisi — Adres, acil durum kişisi, sigorta bilgisi ve profil güncelleme.
 */
@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final InsuranceInfoRepository insuranceInfoRepository;

    public ProfileService(UserRepository userRepository,
                          AddressRepository addressRepository,
                          EmergencyContactRepository emergencyContactRepository,
                          InsuranceInfoRepository insuranceInfoRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.insuranceInfoRepository = insuranceInfoRepository;
    }

    // ===== Profil Güncelleme =====
    public UserResponseDTO updateProfile(String email, ProfileUpdateDTO dto) {
        User user = findUserByEmail(email);

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getTcKimlik() != null) user.setTcKimlik(dto.getTcKimlik());
        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getBloodGroup() != null) user.setBloodGroup(dto.getBloodGroup());
        if (dto.getChronicDiseases() != null) user.setChronicDiseases(dto.getChronicDiseases());
        if (dto.getDisabilities() != null) user.setDisabilities(dto.getDisabilities());

        User saved = userRepository.save(user);
        return mapToFullDTO(saved);
    }

    // ===== Detaylı Profil Getir =====
    public UserResponseDTO getFullProfile(String email) {
        User user = findUserByEmail(email);
        return mapToFullDTO(user);
    }

    // ===== ADRES =====
    public AddressDTO getAddress(String email) {
        User user = findUserByEmail(email);
        Address address = addressRepository.findByUserId(user.getId()).orElse(null);
        if (address == null) return null;
        return mapAddressToDTO(address);
    }

    public AddressDTO saveAddress(String email, AddressDTO dto) {
        User user = findUserByEmail(email);
        Address address = addressRepository.findByUserId(user.getId()).orElse(new Address());
        address.setUser(user);
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setNeighborhood(dto.getNeighborhood());
        address.setFullAddress(dto.getFullAddress());
        address.setPostalCode(dto.getPostalCode());
        Address saved = addressRepository.save(address);
        return mapAddressToDTO(saved);
    }

    // ===== ACİL DURUM KİŞİLERİ =====
    public List<EmergencyContactDTO> getEmergencyContacts(String email) {
        User user = findUserByEmail(email);
        return emergencyContactRepository.findByUserId(user.getId())
                .stream().map(this::mapContactToDTO).collect(Collectors.toList());
    }

    public EmergencyContactDTO addEmergencyContact(String email, EmergencyContactDTO dto) {
        User user = findUserByEmail(email);
        EmergencyContact contact = new EmergencyContact();
        contact.setUser(user);
        contact.setFullName(dto.getFullName());
        contact.setPhone(dto.getPhone());
        contact.setRelationship(dto.getRelationship());
        EmergencyContact saved = emergencyContactRepository.save(contact);
        return mapContactToDTO(saved);
    }

    public void deleteEmergencyContact(String email, Long contactId) {
        User user = findUserByEmail(email);
        EmergencyContact contact = emergencyContactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Acil durum kişisi bulunamadı"));
        if (!contact.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu acil durum kişisi size ait değil");
        }
        emergencyContactRepository.delete(contact);
    }

    // ===== SİGORTA BİLGİSİ =====
    public InsuranceInfoDTO getInsurance(String email) {
        User user = findUserByEmail(email);
        InsuranceInfo info = insuranceInfoRepository.findByUserId(user.getId()).orElse(null);
        if (info == null) return null;
        return mapInsuranceToDTO(info);
    }

    public InsuranceInfoDTO saveInsurance(String email, InsuranceInfoDTO dto) {
        User user = findUserByEmail(email);
        InsuranceInfo info = insuranceInfoRepository.findByUserId(user.getId()).orElse(new InsuranceInfo());
        info.setUser(user);
        info.setInsuranceType(dto.getInsuranceType());
        info.setInsuranceCompany(dto.getInsuranceCompany());
        info.setPolicyNumber(dto.getPolicyNumber());
        info.setSgkNo(dto.getSgkNo());
        info.setNotes(dto.getNotes());
        InsuranceInfo saved = insuranceInfoRepository.save(info);
        return mapInsuranceToDTO(saved);
    }

    // ===== HESAP YÖNETİMİ =====
    public void freezeAccount(String email) {
        User user = findUserByEmail(email);
        user.setAccountStatus(AccountStatus.FROZEN);
        userRepository.save(user);
    }

    public void deleteAccount(String email) {
        User user = findUserByEmail(email);
        user.setAccountStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }

    // ===== YARDIMCI METODLAR =====
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + email));
    }

    public UserResponseDTO mapToFullDTO(User user) {
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

    private AddressDTO mapAddressToDTO(Address a) {
        AddressDTO dto = new AddressDTO();
        dto.setCity(a.getCity());
        dto.setDistrict(a.getDistrict());
        dto.setNeighborhood(a.getNeighborhood());
        dto.setFullAddress(a.getFullAddress());
        dto.setPostalCode(a.getPostalCode());
        return dto;
    }

    private EmergencyContactDTO mapContactToDTO(EmergencyContact c) {
        EmergencyContactDTO dto = new EmergencyContactDTO();
        dto.setId(c.getId());
        dto.setFullName(c.getFullName());
        dto.setPhone(c.getPhone());
        dto.setRelationship(c.getRelationship());
        return dto;
    }

    private InsuranceInfoDTO mapInsuranceToDTO(InsuranceInfo i) {
        InsuranceInfoDTO dto = new InsuranceInfoDTO();
        dto.setInsuranceType(i.getInsuranceType());
        dto.setInsuranceCompany(i.getInsuranceCompany());
        dto.setPolicyNumber(i.getPolicyNumber());
        dto.setSgkNo(i.getSgkNo());
        dto.setNotes(i.getNotes());
        return dto;
    }
}
