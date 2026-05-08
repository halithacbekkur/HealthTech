package com.healthtech.telehealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private long totalUsers;
    private long totalPatients;
    private long totalDoctors;
    private long totalAdmins;

    private long totalAppointments;
    private long pendingAppointments;
    private long approvedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;

    private long totalPrescriptions;
    private long totalMedicalRecords;
}
