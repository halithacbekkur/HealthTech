package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.AppointmentRequestDTO;
import com.healthtech.telehealth.dto.AppointmentResponseDTO;
import com.healthtech.telehealth.entity.Appointment;
import com.healthtech.telehealth.entity.AppointmentStatus;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.AppointmentRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    // Dependency Injection (Bağımlılık Enjeksiyonu): İhtiyacımız olan diğer sınıfları buraya dahil ediyoruz
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    // 1. Yeni Randevu Oluşturma (Create)
    public AppointmentResponseDTO createAppointment(String patientEmail, AppointmentRequestDTO requestDTO) {
        
        // Önce hastayı bul (Email'den)
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı"));

        // Sonra doktoru bul (ID'den)
        User doctor = userRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        // Yeni randevu objesi oluştur ve içini doldur
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING); // İlk oluşturulduğunda durumu "Bekliyor"
        appointment.setCreatedAt(LocalDateTime.now());

        // Veritabanına kaydet
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Kaydedilen veriyi kullanıcıya göstermek için DTO'ya çevir ve geri döndür
        return mapToResponseDTO(savedAppointment);
    }

    // 2. Bir Hastanın Randevularını Getirme
    public List<AppointmentResponseDTO> getPatientAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        
        // Tüm listeyi DTO formatına çevir
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

    // 4. Randevu İptal Etme (Cancel)
    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment updated = appointmentRepository.save(appointment);

        return mapToResponseDTO(updated);
    }

    // 5. Doktor Randevu Onaylama (Approve)
    public AppointmentResponseDTO approveAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        appointment.setStatus(AppointmentStatus.APPROVED);
        Appointment updated = appointmentRepository.save(appointment);

        return mapToResponseDTO(updated);
    }

    // Yardımcı Metod: Veritabanı nesnesini (Entity), DTO nesnesine çevirir
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
