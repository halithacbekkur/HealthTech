package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.ReportDTO;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MessageRepository messageRepository;
    private final NotificationRepository notificationRepository;
    private final VideoCallRepository videoCallRepository;
    private final EmergencyRequestRepository emergencyRequestRepository;
    private final LabResultRepository labResultRepository;
    private final AuditLogRepository auditLogRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    public ReportService(UserRepository userRepository,
                         AppointmentRepository appointmentRepository,
                         PrescriptionRepository prescriptionRepository,
                         MedicalRecordRepository medicalRecordRepository,
                         MessageRepository messageRepository,
                         NotificationRepository notificationRepository,
                         VideoCallRepository videoCallRepository,
                         EmergencyRequestRepository emergencyRequestRepository,
                         LabResultRepository labResultRepository,
                         AuditLogRepository auditLogRepository,
                         DoctorProfileRepository doctorProfileRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.messageRepository = messageRepository;
        this.notificationRepository = notificationRepository;
        this.videoCallRepository = videoCallRepository;
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.labResultRepository = labResultRepository;
        this.auditLogRepository = auditLogRepository;
        this.doctorProfileRepository = doctorProfileRepository;
    }

    public ReportDTO getSystemReport() {
        ReportDTO report = new ReportDTO();

        // Kullanıcı istatistikleri
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

        // Reçete ve tıbbi kayıt
        report.setTotalPrescriptions(prescriptionRepository.count());
        report.setTotalMedicalRecords(medicalRecordRepository.count());

        // Faz 11: Genişletilmiş istatistikler
        report.setTotalMessages(messageRepository.count());
        report.setTotalNotifications(notificationRepository.count());
        report.setTotalVideoCalls(videoCallRepository.count());
        report.setActiveVideoCalls(videoCallRepository.findByStatus(VideoCallStatus.ACTIVE).size()
                + videoCallRepository.findByStatus(VideoCallStatus.WAITING).size());
        report.setTotalEmergencyRequests(emergencyRequestRepository.count());
        report.setPendingEmergencyRequests(emergencyRequestRepository.countByStatus(EmergencyStatus.PENDING));
        report.setTotalLabResults(labResultRepository.count());
        report.setTotalAuditLogs(auditLogRepository.count());

        // Doktor onay istatistikleri
        report.setPendingDoctorApprovals(doctorProfileRepository.findByApprovalStatus(DoctorApprovalStatus.PENDING).size());
        report.setApprovedDoctors(doctorProfileRepository.findByApprovalStatus(DoctorApprovalStatus.APPROVED).size());
        report.setRejectedDoctors(doctorProfileRepository.findByApprovalStatus(DoctorApprovalStatus.REJECTED).size());

        // Son 7 gün randevu trendi
        report.setAppointmentTrend(getAppointmentTrend());

        // Son 7 gün kayıt trendi
        report.setRegistrationTrend(getRegistrationTrend());

        return report;
    }

    private List<Map<String, Object>> getAppointmentTrend() {
        List<Map<String, Object>> trend = new ArrayList<>();
        List<Appointment> allAppointments = appointmentRepository.findAll();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

            long count = allAppointments.stream()
                    .filter(a -> a.getCreatedAt() != null
                            && !a.getCreatedAt().isBefore(dayStart)
                            && !a.getCreatedAt().isAfter(dayEnd))
                    .count();

            Map<String, Object> day = new LinkedHashMap<>();
            day.put("date", date.toString());
            day.put("label", date.getDayOfMonth() + "/" + date.getMonthValue());
            day.put("count", count);
            trend.add(day);
        }
        return trend;
    }

    private List<Map<String, Object>> getRegistrationTrend() {
        List<Map<String, Object>> trend = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

            long count = allUsers.stream()
                    .filter(u -> u.getCreatedAt() != null
                            && !u.getCreatedAt().isBefore(dayStart)
                            && !u.getCreatedAt().isAfter(dayEnd))
                    .count();

            Map<String, Object> day = new LinkedHashMap<>();
            day.put("date", date.toString());
            day.put("label", date.getDayOfMonth() + "/" + date.getMonthValue());
            day.put("count", count);
            trend.add(day);
        }
        return trend;
    }
}
