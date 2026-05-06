package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.PrescriptionRequestDTO;
import com.healthtech.telehealth.dto.PrescriptionResponseDTO;
import com.healthtech.telehealth.entity.Appointment;
import com.healthtech.telehealth.entity.AppointmentStatus;
import com.healthtech.telehealth.entity.Prescription;
import com.healthtech.telehealth.entity.User;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.AppointmentRepository;
import com.healthtech.telehealth.repository.PrescriptionRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               AppointmentRepository appointmentRepository,
                               UserRepository userRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    // Doktorun recete yazmasi
    public PrescriptionResponseDTO createPrescription(String doctorEmail, PrescriptionRequestDTO requestDTO) {
        // Doktoru bul
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadi"));

        // Randevuyu bul
        Appointment appointment = appointmentRepository.findById(requestDTO.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadi"));

        // Randevu bu doktora mi ait kontrol et
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("Bu randevu baska bir doktora ait, recete yazamazsiniz!");
        }

        // Randevu onaylanmis mi? (Pending olan veya Iptal olan randevuya ilac yazilamaz)
        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new RuntimeException("Sadece ONAYLANMIS randevulara recete yazilabilir!");
        }

        // Daha once bu randevuya recete yazilmis mi?
        if (prescriptionRepository.findByAppointmentId(appointment.getId()).isPresent()) {
            throw new RuntimeException("Bu randevuya ait zaten bir recete var!");
        }

        // Yeni recete objesi olustur ve kaydet
        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setMedicines(requestDTO.getMedicines());
        prescription.setInstructions(requestDTO.getInstructions());

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return mapToResponseDTO(savedPrescription);
    }

    // Hastanin kendi recetelerini gormesi
    public List<PrescriptionResponseDTO> getMyPrescriptions(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadi"));

        List<Prescription> prescriptions = prescriptionRepository.findByAppointment_Patient_Id(patient.getId());

        // Gelen listeyi DTO listesine cevirip donduruyoruz
        return prescriptions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Yardimci Metod: Entity'i DTO'ya cevirir
    private PrescriptionResponseDTO mapToResponseDTO(Prescription p) {
        return new PrescriptionResponseDTO(
                p.getId(),
                p.getAppointment().getId(),
                p.getAppointment().getDoctor().getFullName(),
                p.getAppointment().getPatient().getFullName(),
                p.getMedicines(),
                p.getInstructions(),
                p.getCreatedAt()
        );
    }
}
