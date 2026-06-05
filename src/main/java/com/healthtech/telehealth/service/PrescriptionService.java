package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.PrescriptionRequestDTO;
import com.healthtech.telehealth.dto.PrescriptionResponseDTO;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               AppointmentRepository appointmentRepository,
                               UserRepository userRepository,
                               NotificationService notificationService) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // Doktorun reçete yazması
    public PrescriptionResponseDTO createPrescription(String doctorEmail, PrescriptionRequestDTO dto) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadi"));
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadi"));
        if (!appointment.getDoctor().getId().equals(doctor.getId()))
            throw new RuntimeException("Bu randevu baska bir doktora ait!");
        if (appointment.getStatus() != AppointmentStatus.APPROVED && appointment.getStatus() != AppointmentStatus.COMPLETED)
            throw new RuntimeException("Sadece onaylanmış/tamamlanmış randevulara reçete yazılabilir!");

        Prescription p = new Prescription();
        p.setAppointment(appointment);
        p.setMedicines(dto.getMedicines());
        p.setInstructions(dto.getInstructions());
        // Faz 5 alanları
        p.setDosages(dto.getDosages());
        p.setFrequencies(dto.getFrequencies());
        p.setDurationDays(dto.getDurationDays());
        p.setStartDate(dto.getStartDate());
        p.setWarnings(dto.getWarnings());
        p.setDiagnosis(dto.getDiagnosis());
        p.setStatus(PrescriptionStatus.ACTIVE);

        Prescription saved = prescriptionRepository.save(p);

        // Bildirim gönder
        notificationService.send(appointment.getPatient().getId(), "Yeni Reçete",
                "Dr. " + doctor.getFullName() + " size bir reçete yazdı.",
                "PRESCRIPTION", "fa-prescription", "prescriptions");

        return mapToDTO(saved);
    }

    // Hastanın reçeteleri
    public List<PrescriptionResponseDTO> getMyPrescriptions(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadi"));
        return prescriptionRepository.findByAppointment_Patient_Id(patient.getId())
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Doktorun yazdığı reçeteler
    public List<PrescriptionResponseDTO> getDoctorPrescriptions(String doctorEmail) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadi"));
        return prescriptionRepository.findByAppointment_Doctor_Id(doctor.getId())
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Reçete durumu güncelle
    public PrescriptionResponseDTO updateStatus(Long prescriptionId, String status) {
        Prescription p = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Reçete bulunamadı"));
        p.setStatus(PrescriptionStatus.valueOf(status));
        return mapToDTO(prescriptionRepository.save(p));
    }

    private PrescriptionResponseDTO mapToDTO(Prescription p) {
        return new PrescriptionResponseDTO(
                p.getId(), p.getAppointment().getId(),
                p.getAppointment().getDoctor().getFullName(),
                p.getAppointment().getPatient().getFullName(),
                p.getMedicines(), p.getInstructions(),
                p.getDosages(), p.getFrequencies(), p.getDurationDays(),
                p.getStartDate(), p.getEndDate(),
                p.getStatus() != null ? p.getStatus().name() : "ACTIVE",
                p.getWarnings(), p.getDiagnosis(), p.getCreatedAt()
        );
    }
}
