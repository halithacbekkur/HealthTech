package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.EmergencyCreateDTO;
import com.healthtech.telehealth.dto.EmergencyRequestDTO;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.EmergencyRequestRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Faz 10: Acil Durum Servisi — SOS talebi oluşturma, doktor atama, çözümleme.
 */
@Service
public class EmergencyService {

    private final EmergencyRequestRepository emergencyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public EmergencyService(EmergencyRequestRepository emergencyRepository,
                            UserRepository userRepository,
                            NotificationService notificationService) {
        this.emergencyRepository = emergencyRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    /**
     * Hasta SOS talebi oluşturur.
     */
    public EmergencyRequestDTO createEmergency(String patientEmail, EmergencyCreateDTO dto) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı"));

        EmergencyRequest req = new EmergencyRequest();
        req.setPatient(patient);
        req.setLevel(EmergencyLevel.valueOf(dto.getLevel() != null ? dto.getLevel() : "MEDIUM"));
        req.setDescription(dto.getDescription());
        req.setSymptoms(dto.getSymptoms());
        req.setLocation(dto.getLocation());
        req.setStatus(EmergencyStatus.PENDING);

        req = emergencyRepository.save(req);

        // Tüm doktorlara bildirim gönder
        List<User> doctors = userRepository.findByRole(Role.DOCTOR);
        for (User doc : doctors) {
            notificationService.send(
                    doc.getId(), "🚨 Acil Durum Talebi",
                    patient.getFullName() + " acil yardım talep etti! Seviye: " + req.getLevel(),
                    "EMERGENCY", "fa-triangle-exclamation", "/emergency"
            );
        }

        return mapToDTO(req);
    }

    /**
     * Doktor acil talebi üstlenir (respond).
     */
    public EmergencyRequestDTO respondToEmergency(Long requestId, String doctorEmail) {
        EmergencyRequest req = emergencyRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Acil durum talebi bulunamadı"));
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        if (req.getStatus() != EmergencyStatus.PENDING) {
            throw new RuntimeException("Bu talep zaten yanıtlanmış");
        }

        req.setAssignedDoctor(doctor);
        req.setStatus(EmergencyStatus.RESPONDING);
        req.setRespondedAt(LocalDateTime.now());
        emergencyRepository.save(req);

        // Hastaya bildirim
        notificationService.send(
                req.getPatient().getId(), "Acil Durum Yanıtı",
                "Dr. " + doctor.getFullName() + " acil talebinizi üstlendi!",
                "EMERGENCY", "fa-user-doctor", "/emergency"
        );

        return mapToDTO(req);
    }

    /**
     * Doktor acil durumu çözer.
     */
    public EmergencyRequestDTO resolveEmergency(Long requestId, String notes, String doctorEmail) {
        EmergencyRequest req = emergencyRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Acil durum talebi bulunamadı"));
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        if (req.getAssignedDoctor() == null || !req.getAssignedDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("Bu talebi sadece üstlenen doktor çözebilir");
        }

        req.setStatus(EmergencyStatus.RESOLVED);
        req.setResolvedAt(LocalDateTime.now());
        req.setResponseNotes(notes);
        emergencyRepository.save(req);

        // Hastaya bildirim
        notificationService.send(
                req.getPatient().getId(), "Acil Durum Çözüldü",
                "Dr. " + doctor.getFullName() + " acil durumunuzu çözdü.",
                "EMERGENCY", "fa-check-circle", "/emergency"
        );

        return mapToDTO(req);
    }

    /**
     * Hasta acil talebi iptal eder.
     */
    public EmergencyRequestDTO cancelEmergency(Long requestId, String patientEmail) {
        EmergencyRequest req = emergencyRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Acil durum talebi bulunamadı"));
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı"));

        if (!req.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("Bu talebi sadece oluşturan hasta iptal edebilir");
        }

        req.setStatus(EmergencyStatus.CANCELLED);
        emergencyRepository.save(req);
        return mapToDTO(req);
    }

    /**
     * Hastanın acil taleplerini getir.
     */
    public List<EmergencyRequestDTO> getMyEmergencies(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı"));
        return emergencyRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Tüm bekleyen acil talepleri getir (doktor/admin).
     */
    public List<EmergencyRequestDTO> getPendingEmergencies() {
        return emergencyRepository.findByStatusOrderByCreatedAtDesc(EmergencyStatus.PENDING)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Tüm acil talepleri getir (admin).
     */
    public List<EmergencyRequestDTO> getAllEmergencies() {
        return emergencyRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Doktorun üstlendiği talepleri getir.
     */
    public List<EmergencyRequestDTO> getMyAssignedEmergencies(String doctorEmail) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));
        return emergencyRepository.findByAssignedDoctorIdOrderByCreatedAtDesc(doctor.getId())
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Bekleyen acil talep sayısı.
     */
    public long getPendingCount() {
        return emergencyRepository.countByStatus(EmergencyStatus.PENDING);
    }

    private EmergencyRequestDTO mapToDTO(EmergencyRequest req) {
        EmergencyRequestDTO dto = new EmergencyRequestDTO();
        dto.setId(req.getId());
        dto.setPatientName(req.getPatient().getFullName());
        dto.setPatientId(req.getPatient().getId());
        dto.setLevel(req.getLevel().name());
        dto.setDescription(req.getDescription());
        dto.setSymptoms(req.getSymptoms());
        dto.setLocation(req.getLocation());
        dto.setStatus(req.getStatus().name());
        dto.setResponseNotes(req.getResponseNotes());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setRespondedAt(req.getRespondedAt());
        dto.setResolvedAt(req.getResolvedAt());
        if (req.getAssignedDoctor() != null) {
            dto.setAssignedDoctorName(req.getAssignedDoctor().getFullName());
            dto.setAssignedDoctorId(req.getAssignedDoctor().getId());
        }
        return dto;
    }
}
