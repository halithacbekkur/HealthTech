package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.VideoCall;
import com.healthtech.telehealth.entity.VideoCallStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoCallRepository extends JpaRepository<VideoCall, Long> {

    Optional<VideoCall> findByRoomId(String roomId);

    List<VideoCall> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);

    List<VideoCall> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    Optional<VideoCall> findByAppointmentId(Long appointmentId);

    List<VideoCall> findByDoctorIdAndStatus(Long doctorId, VideoCallStatus status);

    List<VideoCall> findByPatientIdAndStatus(Long patientId, VideoCallStatus status);

    List<VideoCall> findByStatus(VideoCallStatus status);
}
