package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.AppointmentRequestDTO;
import com.healthtech.telehealth.dto.AppointmentResponseDTO;
import com.healthtech.telehealth.entity.Appointment;
import com.healthtech.telehealth.entity.AppointmentStatus;
import com.healthtech.telehealth.entity.Role;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.AppointmentNotFoundException;
import com.healthtech.telehealth.exception.InvalidStatusTransitionException;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.AppointmentRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    // 1. Yeni Randevu Oluşturma
    public AppointmentResponseDTO createAppointment(String patientEmail, AppointmentRequestDTO requestDTO) {

        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı: " + patientEmail));

        User doctor = userRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı: ID " + requestDTO.getDoctorId()));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new InvalidStatusTransitionException("Seçilen kullanıcı doktor değil: " + doctor.getFullName());
        }

        if (requestDTO.getAppointmentDate() == null) {
            throw new InvalidStatusTransitionException("Randevu tarihi boş olamaz");
        }

        if (requestDTO.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new InvalidStatusTransitionException("Geçmiş bir tarihe randevu oluşturulamaz");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setNotes(requestDTO.getNotes());
        appointment.setCreatedAt(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);
        return mapToResponseDTO(saved);
    }

    // 2. Hastanın kendi randevuları (email üzerinden — güvenli)
    public List<AppointmentResponseDTO> getPatientAppointmentsByEmail(String email) {
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı: " + email));
        return appointmentRepository.findByPatientId(patient.getId())
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // 3. Doktorun randevuları (email üzerinden — güvenli)
    public List<AppointmentResponseDTO> getDoctorAppointmentsByEmail(String email) {
        User doctor = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı: " + email));
        return appointmentRepository.findByDoctorId(doctor.getId())
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // 4. ID ile randevu getir
    public List<AppointmentResponseDTO> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // 5. Randevu İptal
    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidStatusTransitionException("Tamamlanmış randevu iptal edilemez");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Randevu zaten iptal edilmiş");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return mapToResponseDTO(appointmentRepository.save(appointment));
    }

    // 6. Doktor Randevu Onaylama
    public AppointmentResponseDTO approveAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Sadece bekleyen randevular onaylanabilir. Mevcut durum: " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.APPROVED);
        return mapToResponseDTO(appointmentRepository.save(appointment));
    }

    // 7. Randevu Tamamlama
    public AppointmentResponseDTO completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new InvalidStatusTransitionException(
                    "Sadece onaylanmış randevular tamamlanabilir. Mevcut durum: " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return mapToResponseDTO(appointmentRepository.save(appointment));
    }

    // Yardımcı: Entity → DTO
    private AppointmentResponseDTO mapToResponseDTO(Appointment a) {
        return new AppointmentResponseDTO(
                a.getId(),
                a.getPatient().getId(),
                a.getPatient().getFullName(),
                a.getDoctor().getId(),
                a.getDoctor().getFullName(),
                a.getAppointmentDate(),
                a.getStatus(),
                a.getNotes(),
                a.getCreatedAt()
        );
    }
}
