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

        // Hastayı bul
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı: " + patientEmail));

        // Doktoru bul
        User doctor = userRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı: ID " + requestDTO.getDoctorId()));

        // Doktor rolü kontrolü — sadece DOCTOR rolündeki kullanıcıya randevu alınabilir
        if (doctor.getRole() != Role.DOCTOR) {
            throw new InvalidStatusTransitionException("Seçilen kullanıcı doktor değil: " + doctor.getFullName());
        }

        // Geçmiş tarih kontrolü — geçmiş bir tarihe randevu alınamaz
        if (requestDTO.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new InvalidStatusTransitionException("Geçmiş bir tarihe randevu oluşturulamaz");
        }

        // Randevu oluştur
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setCreatedAt(LocalDateTime.now());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToResponseDTO(savedAppointment);
    }

    // 2. Bir Hastanın Randevularını Getirme
    public List<AppointmentResponseDTO> getPatientAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. Bir Doktorun Randevularını Getirme
    public List<AppointmentResponseDTO> getDoctorAppointments(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 4. Randevu İptal Etme — sadece PENDING veya APPROVED durumdaki randevular iptal edilebilir
    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));

        // Durum geçiş kontrolü: Sadece PENDING veya APPROVED iken iptal edilebilir
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidStatusTransitionException("Tamamlanmış randevu iptal edilemez");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Randevu zaten iptal edilmiş");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment updated = appointmentRepository.save(appointment);
        return mapToResponseDTO(updated);
    }

    // 5. Doktor Randevu Onaylama — sadece PENDING durumdaki randevular onaylanabilir
    public AppointmentResponseDTO approveAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));

        // Durum geçiş kontrolü: Sadece PENDING iken onaylanabilir
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Sadece bekleyen (PENDING) randevular onaylanabilir. Mevcut durum: " + appointment.getStatus()
            );
        }

        appointment.setStatus(AppointmentStatus.APPROVED);
        Appointment updated = appointmentRepository.save(appointment);
        return mapToResponseDTO(updated);
    }

    // 6. Randevu Tamamlama — sadece APPROVED durumdaki randevular tamamlanabilir
    public AppointmentResponseDTO completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));

        // Durum geçiş kontrolü: Sadece APPROVED iken tamamlanabilir
        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new InvalidStatusTransitionException(
                    "Sadece onaylanmış (APPROVED) randevular tamamlanabilir. Mevcut durum: " + appointment.getStatus()
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment updated = appointmentRepository.save(appointment);
        return mapToResponseDTO(updated);
    }

    // Yardımcı: Entity → DTO dönüşümü
    private AppointmentResponseDTO mapToResponseDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getPatient().getFullName(),
                appointment.getDoctor().getFullName(),
                appointment.getAppointmentDate(),
                appointment.getStatus()
        );
    }
}
