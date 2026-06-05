package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final LabResultRepository labResultRepository;
    private final UserRepository userRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                LabResultRepository labResultRepository,
                                UserRepository userRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.labResultRepository = labResultRepository;
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

    // 2. Tibbi Kayit Olustur veya Guncelle
    public MedicalRecordResponseDTO createOrUpdateRecord(String email, MedicalRecordRequestDTO dto) {
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanici bulunamadi"));
        MedicalRecord record = medicalRecordRepository.findByPatientId(patient.getId())
                .orElse(new MedicalRecord());

        record.setPatient(patient);
        record.setBloodGroup(dto.getBloodGroup());
        record.setAllergies(dto.getAllergies());
        record.setPastDiseases(dto.getPastDiseases());
        record.setHeight(dto.getHeight());
        record.setWeight(dto.getWeight());
        // Faz 4: Genişletilmiş alanlar
        record.setChronicDiseases(dto.getChronicDiseases());
        record.setSurgeryHistory(dto.getSurgeryHistory());
        record.setFamilyHistory(dto.getFamilyHistory());
        record.setCurrentMedications(dto.getCurrentMedications());
        record.setDisabilities(dto.getDisabilities());
        record.setSmoker(dto.getSmoker());
        record.setAlcoholUse(dto.getAlcoholUse());

        MedicalRecord saved = medicalRecordRepository.save(record);
        return mapToResponseDTO(saved);
    }

    // ===== FAZ 4: LABORATUVAR SONUÇLARI =====
    public LabResultDTO addLabResult(String doctorEmail, Long patientId, LabResultDTO dto) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadi"));
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadi"));

        LabResult lab = new LabResult();
        lab.setPatient(patient);
        lab.setDoctor(doctor);
        lab.setTestName(dto.getTestName());
        lab.setTestCategory(dto.getTestCategory());
        lab.setResults(dto.getResults());
        lab.setResultValue(dto.getResultValue());
        lab.setUnit(dto.getUnit());
        lab.setReferenceRange(dto.getReferenceRange());
        lab.setStatus(dto.getStatus() != null ? LabResultStatus.valueOf(dto.getStatus()) : LabResultStatus.NORMAL);
        lab.setTestDate(dto.getTestDate());
        lab.setLaboratory(dto.getLaboratory());
        lab.setFileUrl(dto.getFileUrl());
        lab.setNotes(dto.getNotes());

        LabResult saved = labResultRepository.save(lab);
        return toLabDTO(saved);
    }

    public List<LabResultDTO> getPatientLabResults(Long patientId) {
        return labResultRepository.findByPatientIdOrderByTestDateDesc(patientId)
                .stream().map(this::toLabDTO).collect(Collectors.toList());
    }

    public List<LabResultDTO> getMyLabResults(String email) {
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadi"));
        return labResultRepository.findByPatientIdOrderByTestDateDesc(patient.getId())
                .stream().map(this::toLabDTO).collect(Collectors.toList());
    }

    // Yardımcı: MedicalRecord -> DTO
    private MedicalRecordResponseDTO mapToResponseDTO(MedicalRecord r) {
        return new MedicalRecordResponseDTO(
                r.getId(), r.getPatient().getFullName(), r.getPatient().getEmail(),
                r.getBloodGroup(), r.getAllergies(), r.getPastDiseases(),
                r.getHeight(), r.getWeight(),
                r.getChronicDiseases(), r.getSurgeryHistory(), r.getFamilyHistory(),
                r.getCurrentMedications(), r.getDisabilities(),
                r.getSmoker(), r.getAlcoholUse(), r.getUpdatedAt()
        );
    }

    // Yardımcı: LabResult -> DTO
    private LabResultDTO toLabDTO(LabResult l) {
        return new LabResultDTO(
                l.getId(), l.getTestName(), l.getTestCategory(),
                l.getResults(), l.getResultValue(), l.getUnit(), l.getReferenceRange(),
                l.getStatus() != null ? l.getStatus().name() : "NORMAL",
                l.getTestDate(), l.getLaboratory(), l.getFileUrl(), l.getNotes(),
                l.getDoctor() != null ? l.getDoctor().getFullName() : null,
                l.getCreatedAt()
        );
    }
}
