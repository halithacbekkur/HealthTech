package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.ReportDTO;
import com.healthtech.telehealth.entity.AppointmentStatus;
import com.healthtech.telehealth.entity.Role;
import com.healthtech.telehealth.repository.AppointmentRepository;
import com.healthtech.telehealth.repository.MedicalRecordRepository;
import com.healthtech.telehealth.repository.PrescriptionRepository;
import com.healthtech.telehealth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ReportService(UserRepository userRepository,
                         AppointmentRepository appointmentRepository,
                         PrescriptionRepository prescriptionRepository,
                         MedicalRecordRepository medicalRecordRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public ReportDTO getSystemReport() {
        ReportDTO report = new ReportDTO();

        // Kullanici istatistikleri
        report.setTotalUsers(userRepository.count());
        report.setTotalPatients(userRepository.findByRole(Role.PATIENT).size());
        report.setTotalDoctors(userRepository.findByRole(Role.DOCTOR).size());
        report.setTotalAdmins(userRepository.findByRole(Role.ADMIN).size());

        // Randevu istatistikleri
        report.setTotalAppointments(appointmentRepository.count());
        report.setPendingAppointments(appointmentRepository.countByStatus(AppointmentStatus.PENDING));
        report.setApprovedAppointments(appointmentRepository.countByStatus(AppointmentStatus.APPROVED));
        report.setCompletedAppointments(appointmentRepository.countByStatus(AppointmentStatus.COMPLETED));
        report.setCancelledAppointments(appointmentRepository.countByStatus(AppointmentStatus.CANCELLED));

        // Recete ve tibbi kayit
        report.setTotalPrescriptions(prescriptionRepository.count());
        report.setTotalMedicalRecords(medicalRecordRepository.count());

        return report;
    }
}
