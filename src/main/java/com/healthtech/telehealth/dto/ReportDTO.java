package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    // Kullanıcı istatistikleri
    private long totalUsers;
    private long totalPatients;
    private long totalDoctors;
    private long totalAdmins;

    // Randevu istatistikleri
    private long totalAppointments;
    private long pendingAppointments;
    private long approvedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;

    // Tıbbi kayıt istatistikleri
    private long totalPrescriptions;
    private long totalMedicalRecords;

    // Faz 11: Genişletilmiş istatistikler
    private long totalMessages;
    private long totalNotifications;
    private long totalVideoCalls;
    private long activeVideoCalls;
    private long totalEmergencyRequests;
    private long pendingEmergencyRequests;
    private long totalLabResults;
    private long totalAuditLogs;

    // Doktor onay istatistikleri
    private long pendingDoctorApprovals;
    private long approvedDoctors;
    private long rejectedDoctors;

    // Son 7 gün trend verileri
    private List<Map<String, Object>> appointmentTrend;
    private List<Map<String, Object>> registrationTrend;
}
