package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.VideoCallDTO;
import com.healthtech.telehealth.dto.VideoCallRequestDTO;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.exception.UserNotFoundException;
import com.healthtech.telehealth.repository.AppointmentRepository;
import com.healthtech.telehealth.repository.UserRepository;
import com.healthtech.telehealth.repository.VideoCallRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Faz 9: Video Görüşme Servisi — Oda oluşturma, başlatma, sonlandırma.
 */
@Service
public class VideoCallService {

    private final VideoCallRepository videoCallRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public VideoCallService(VideoCallRepository videoCallRepository,
                            UserRepository userRepository,
                            AppointmentRepository appointmentRepository,
                            NotificationService notificationService) {
        this.videoCallRepository = videoCallRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
    }

    /**
     * Doktor video görüşme odası oluşturur.
     */
    public VideoCallDTO createVideoCall(String doctorEmail, VideoCallRequestDTO request) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        User patient;
        Appointment appointment = null;

        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
            patient = appointment.getPatient();
        } else if (request.getPatientId() != null) {
            patient = userRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı"));
        } else {
            throw new RuntimeException("Hasta ID veya Randevu ID gereklidir");
        }

        VideoCall call = new VideoCall();
        call.setDoctor(doctor);
        call.setPatient(patient);
        call.setAppointment(appointment);
        call.setStatus(VideoCallStatus.WAITING);
        call.setScheduledAt(request.getScheduledAt() != null ? request.getScheduledAt() : LocalDateTime.now());

        call = videoCallRepository.save(call);

        // Hastaya bildirim gönder
        notificationService.send(
                patient.getId(), "Video Görüşme Daveti",
                "Dr. " + doctor.getFullName() + " sizi video görüşmeye davet etti.",
                "VIDEO_CALL", "fa-video", "/video-call/" + call.getRoomId()
        );

        return mapToDTO(call);
    }

    /**
     * Görüşmeye katıl (hasta veya doktor).
     */
    public VideoCallDTO joinCall(String roomId, String userEmail) {
        VideoCall call = videoCallRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Video arama odası bulunamadı"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        // Yetki kontrolü
        if (!call.getDoctor().getId().equals(user.getId()) &&
                !call.getPatient().getId().equals(user.getId())) {
            throw new RuntimeException("Bu görüşmeye erişim yetkiniz yok");
        }

        // İlk katılımda durumu ACTIVE yap
        if (call.getStatus() == VideoCallStatus.WAITING) {
            call.setStatus(VideoCallStatus.ACTIVE);
            call.setStartedAt(LocalDateTime.now());
            videoCallRepository.save(call);
        }

        return mapToDTO(call);
    }

    /**
     * Görüşmeyi sonlandır.
     */
    public VideoCallDTO endCall(String roomId, String userEmail) {
        VideoCall call = videoCallRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Video arama odası bulunamadı"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        if (!call.getDoctor().getId().equals(user.getId()) &&
                !call.getPatient().getId().equals(user.getId())) {
            throw new RuntimeException("Bu görüşmeyi sonlandırma yetkiniz yok");
        }

        call.setStatus(VideoCallStatus.COMPLETED);
        call.setEndedAt(LocalDateTime.now());
        if (call.getStartedAt() != null) {
            call.setDurationSeconds(
                    (int) java.time.Duration.between(call.getStartedAt(), call.getEndedAt()).getSeconds()
            );
        }
        videoCallRepository.save(call);

        return mapToDTO(call);
    }

    /**
     * Görüşme notlarını kaydet.
     */
    public VideoCallDTO saveNotes(Long callId, String notes, String doctorEmail) {
        VideoCall call = videoCallRepository.findById(callId)
                .orElseThrow(() -> new RuntimeException("Video arama bulunamadı"));
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        if (!call.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("Sadece doktor not ekleyebilir");
        }

        call.setNotes(notes);
        videoCallRepository.save(call);
        return mapToDTO(call);
    }

    /**
     * Kullanıcının tüm görüşmelerini getir.
     */
    public List<VideoCallDTO> getMyCalls(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        List<VideoCall> calls;
        if (user.getRole() == Role.DOCTOR) {
            calls = videoCallRepository.findByDoctorIdOrderByCreatedAtDesc(user.getId());
        } else {
            calls = videoCallRepository.findByPatientIdOrderByCreatedAtDesc(user.getId());
        }
        return calls.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Aktif görüşmeleri getir (bekleme + devam eden).
     */
    public List<VideoCallDTO> getActiveCalls(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        List<VideoCall> waitingCalls;
        List<VideoCall> activeCalls;
        if (user.getRole() == Role.DOCTOR) {
            waitingCalls = videoCallRepository.findByDoctorIdAndStatus(user.getId(), VideoCallStatus.WAITING);
            activeCalls = videoCallRepository.findByDoctorIdAndStatus(user.getId(), VideoCallStatus.ACTIVE);
        } else {
            waitingCalls = videoCallRepository.findByPatientIdAndStatus(user.getId(), VideoCallStatus.WAITING);
            activeCalls = videoCallRepository.findByPatientIdAndStatus(user.getId(), VideoCallStatus.ACTIVE);
        }
        waitingCalls.addAll(activeCalls);
        return waitingCalls.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Belirli bir görüşme bilgisini getir.
     */
    public VideoCallDTO getCallByRoom(String roomId) {
        VideoCall call = videoCallRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Video arama odası bulunamadı"));
        return mapToDTO(call);
    }

    private VideoCallDTO mapToDTO(VideoCall call) {
        VideoCallDTO dto = new VideoCallDTO();
        dto.setId(call.getId());
        dto.setRoomId(call.getRoomId());
        dto.setDoctorName(call.getDoctor().getFullName());
        dto.setPatientName(call.getPatient().getFullName());
        dto.setDoctorId(call.getDoctor().getId());
        dto.setPatientId(call.getPatient().getId());
        dto.setStatus(call.getStatus().name());
        dto.setScheduledAt(call.getScheduledAt());
        dto.setStartedAt(call.getStartedAt());
        dto.setEndedAt(call.getEndedAt());
        dto.setDurationSeconds(call.getDurationSeconds());
        dto.setNotes(call.getNotes());
        dto.setCreatedAt(call.getCreatedAt());
        if (call.getAppointment() != null) {
            dto.setAppointmentId(call.getAppointment().getId());
        }
        return dto;
    }
}
