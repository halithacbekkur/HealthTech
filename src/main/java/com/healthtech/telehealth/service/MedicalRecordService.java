package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.MedicalRecordRequestDTO;
import com.healthtech.telehealth.dto.MedicalRecordResponseDTO;
import com.healthtech.telehealth.entity.MedicalRecord;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.MedicalRecordRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final UserRepository userRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, UserRepository userRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.userRepository = userRepository;
    }

    // 1. Tibbi Kayit Getir
    public MedicalRecordResponseDTO getRecordByPatientEmail(String email) {
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanici bulunamadi"));

        MedicalRecord record = medicalRecordRepository.findByPatientId(patient.getId())
                .orElseThrow(() -> new RuntimeException("Bu hastaya ait tibbi kayit bulunamadi"));

        return mapToResponseDTO(record);
    }
    
    // Doktorlarin kullanimi icin ID ile getirme
    public MedicalRecordResponseDTO getRecordByPatientId(Long patientId) {
        MedicalRecord record = medicalRecordRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Bu hastaya ait tibbi kayit bulunamadi"));

        return mapToResponseDTO(record);
    }

    // 2. Tibbi Kayit Olustur veya Guncelle (Hastanin Kendisi Yapar)
    public MedicalRecordResponseDTO createOrUpdateRecord(String email, MedicalRecordRequestDTO requestDTO) {
        
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanici bulunamadi"));

        // Hastanin var olan kaydini ara
        MedicalRecord record = medicalRecordRepository.findByPatientId(patient.getId())
                .orElse(new MedicalRecord()); // Eger yoksa yeni (bos) bir kayit objesi olustur

        // Verileri doldur veya guncelle
        record.setPatient(patient);
        record.setBloodGroup(requestDTO.getBloodGroup());
        record.setAllergies(requestDTO.getAllergies());
        record.setPastDiseases(requestDTO.getPastDiseases());
        record.setHeight(requestDTO.getHeight());
        record.setWeight(requestDTO.getWeight());
        record.setUpdatedAt(LocalDateTime.now()); // Guncellenme tarihini su anki zaman yap

        // Veritabanina kaydet
        MedicalRecord savedRecord = medicalRecordRepository.save(record);

        return mapToResponseDTO(savedRecord);
    }

    // Yardimci Metod: Entity'i DTO'ya cevir
    private MedicalRecordResponseDTO mapToResponseDTO(MedicalRecord record) {
        return new MedicalRecordResponseDTO(
                record.getId(),
                record.getPatient().getFullName(),
                record.getPatient().getEmail(),
                record.getBloodGroup(),
                record.getAllergies(),
                record.getPastDiseases(),
                record.getHeight(),
                record.getWeight(),
                record.getUpdatedAt()
        );
    }
}
