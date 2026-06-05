package com.healthtech.telehealth.service;

import com.healthtech.telehealth.dto.*;
import com.healthtech.telehealth.entity.*;
import com.healthtech.telehealth.exception.*;
import com.healthtech.telehealth.repository.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorDayOffRepository dayOffRepository;
    private final NotificationService notificationService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserRepository userRepository,
                              DoctorScheduleRepository scheduleRepository,
                              DoctorDayOffRepository dayOffRepository,
                              NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.dayOffRepository = dayOffRepository;
        this.notificationService = notificationService;
    }

    // 1. Yeni Randevu Oluşturma (çakışma kontrolü ile)
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

        // Faz 3: Çakışma kontrolü
        int slotMinutes = getSlotDuration(doctor.getId(), requestDTO.getAppointmentDate());
        LocalDateTime slotEnd = requestDTO.getAppointmentDate().plusMinutes(slotMinutes);

        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                doctor.getId(),
                requestDTO.getAppointmentDate().minusMinutes(slotMinutes - 1),
                slotEnd);

        if (!conflicts.isEmpty()) {
            throw new InvalidStatusTransitionException("Bu saatte doktorun başka bir randevusu var. Lütfen farklı bir saat seçin.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setNotes(requestDTO.getNotes());
        appointment.setCreatedAt(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);

        // Bildirim: Doktora yeni randevu bildirimi
        notificationService.sendAppointmentNotification(doctor.getId(),
                patient.getFullName() + " yeni bir randevu talebi oluşturdu.");

        return mapToResponseDTO(saved);
    }

    // 2. Hastanın kendi randevuları
    public List<AppointmentResponseDTO> getPatientAppointmentsByEmail(String email) {
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Hasta bulunamadı: " + email));
        return appointmentRepository.findByPatientId(patient.getId())
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // 3. Doktorun randevuları
    public List<AppointmentResponseDTO> getDoctorAppointmentsByEmail(String email) {
        User doctor = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı: " + email));
        return appointmentRepository.findByDoctorId(doctor.getId())
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // 4. ID ile randevu listele
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
        if (appointment.getStatus() == AppointmentStatus.COMPLETED)
            throw new InvalidStatusTransitionException("Tamamlanmış randevu iptal edilemez");
        if (appointment.getStatus() == AppointmentStatus.CANCELLED)
            throw new InvalidStatusTransitionException("Randevu zaten iptal edilmiş");
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);

        // Bildirim: Her iki tarafa da iptal bildirimi
        notificationService.sendAppointmentNotification(appointment.getDoctor().getId(),
                appointment.getPatient().getFullName() + " randevusunu iptal etti.");
        notificationService.sendAppointmentNotification(appointment.getPatient().getId(),
                "Dr. " + appointment.getDoctor().getFullName() + " ile randevunuz iptal edildi.");

        return mapToResponseDTO(saved);
    }

    // 6. Doktor Randevu Onaylama
    public AppointmentResponseDTO approveAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));
        if (appointment.getStatus() != AppointmentStatus.PENDING)
            throw new InvalidStatusTransitionException("Sadece bekleyen randevular onaylanabilir");
        appointment.setStatus(AppointmentStatus.APPROVED);
        Appointment saved = appointmentRepository.save(appointment);

        // Bildirim: Hastaya onay bildirimi
        notificationService.sendAppointmentNotification(appointment.getPatient().getId(),
                "Randevunuz onaylandı! Dr. " + appointment.getDoctor().getFullName());

        return mapToResponseDTO(saved);
    }

    // 7. Randevu Tamamlama
    public AppointmentResponseDTO completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));
        if (appointment.getStatus() != AppointmentStatus.APPROVED)
            throw new InvalidStatusTransitionException("Sadece onaylanmış randevular tamamlanabilir");
        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);

        // Bildirim: Hastaya tamamlanma bildirimi
        notificationService.sendAppointmentNotification(appointment.getPatient().getId(),
                "Randevunuz tamamlandı. Dr. " + appointment.getDoctor().getFullName());

        return mapToResponseDTO(saved);
    }

    // 8. Randevu Erteleme (tarih güncelleme)
    public AppointmentResponseDTO rescheduleAppointment(Long appointmentId, LocalDateTime newDate) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: ID " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED)
            throw new InvalidStatusTransitionException("Tamamlanmış/iptal edilmiş randevu ertelenemez");
        if (newDate.isBefore(LocalDateTime.now()))
            throw new InvalidStatusTransitionException("Geçmiş tarihe erteleme yapılamaz");

        // Çakışma kontrolü
        int slotMinutes = getSlotDuration(appointment.getDoctor().getId(), newDate);
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                appointment.getDoctor().getId(),
                newDate.minusMinutes(slotMinutes - 1),
                newDate.plusMinutes(slotMinutes));

        // Kendi randevusunu hariç tut
        conflicts.removeIf(a -> a.getId().equals(appointmentId));

        if (!conflicts.isEmpty())
            throw new InvalidStatusTransitionException("Yeni saatte çakışma var");

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.PENDING); // Tekrar onay gerekir
        return mapToResponseDTO(appointmentRepository.save(appointment));
    }

    // ===== FAZ 3: DOKTOR TAKVİMİ =====
    public List<DoctorScheduleDTO> getDoctorSchedule(Long doctorId) {
        return scheduleRepository.findByDoctorIdOrderByDayOfWeek(doctorId)
                .stream()
                .map(s -> new DoctorScheduleDTO(s.getId(), s.getDayOfWeek(), s.getStartTime(),
                        s.getEndTime(), s.getSlotDurationMinutes(), s.isAvailable()))
                .collect(Collectors.toList());
    }

    public DoctorScheduleDTO saveSchedule(String doctorEmail, DoctorScheduleDTO dto) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndDayOfWeek(doctor.getId(), dto.getDayOfWeek())
                .orElse(new DoctorSchedule());

        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setSlotDurationMinutes(dto.getSlotDurationMinutes() > 0 ? dto.getSlotDurationMinutes() : 30);
        schedule.setAvailable(dto.isAvailable());

        DoctorSchedule saved = scheduleRepository.save(schedule);
        return new DoctorScheduleDTO(saved.getId(), saved.getDayOfWeek(), saved.getStartTime(),
                saved.getEndTime(), saved.getSlotDurationMinutes(), saved.isAvailable());
    }

    // Müsait slotları hesapla (belirli tarih için)
    public List<AvailableSlotDTO> getAvailableSlots(Long doctorId, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // ★ İzin günü kontrolü — doktor bu tarihte izinliyse boş liste döndür
        if (dayOffRepository.existsByDoctorIdAndOffDate(doctorId, date)) {
            return Collections.emptyList();
        }

        // Doktorun o gün için takvimini bul
        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
                .orElse(null);

        if (schedule == null || !schedule.isAvailable()) return Collections.emptyList();

        // O gündeki mevcut randevuları al
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
        List<Appointment> existing = appointmentRepository.findDoctorAppointmentsForDay(doctorId, dayStart, dayEnd);

        Set<LocalDateTime> bookedTimes = existing.stream()
                .map(Appointment::getAppointmentDate)
                .collect(Collectors.toSet());

        // Slotları oluştur
        List<AvailableSlotDTO> slots = new ArrayList<>();
        LocalTime current = schedule.getStartTime();
        int duration = schedule.getSlotDurationMinutes();

        while (current.plusMinutes(duration).compareTo(schedule.getEndTime()) <= 0) {
            LocalDateTime slotDateTime = LocalDateTime.of(date, current);

            // Geçmiş saatleri ve dolu slotları atla
            if (slotDateTime.isAfter(LocalDateTime.now()) && !bookedTimes.contains(slotDateTime)) {
                slots.add(new AvailableSlotDTO(slotDateTime, duration));
            }
            current = current.plusMinutes(duration);
        }
        return slots;
    }

    /**
     * En yakın müsait randevu önerisi — Bugünden itibaren 30 gün içinde ilk boş slot'u bulur.
     */
    public Map<String, Object> getNearestAvailableSlot(Long doctorId) {
        LocalDate today = LocalDate.now();
        Map<String, Object> result = new LinkedHashMap<>();

        for (int i = 0; i < 30; i++) {
            LocalDate date = today.plusDays(i);
            List<AvailableSlotDTO> slots = getAvailableSlots(doctorId, date);
            if (!slots.isEmpty()) {
                AvailableSlotDTO first = slots.get(0);
                result.put("found", true);
                result.put("date", date.toString());
                result.put("time", first.getDateTime().toLocalTime().toString());
                result.put("dateTime", first.getDateTime().toString());
                result.put("durationMinutes", first.getDurationMinutes());
                result.put("totalSlotsOnDay", slots.size());
                return result;
            }
        }

        result.put("found", false);
        result.put("message", "Önümüzdeki 30 gün içinde müsait randevu bulunamadı.");
        return result;
    }

    /**
     * Haftalık doluluk özeti — 7 gün için her günün slot durumunu döndürür.
     */
    public List<Map<String, Object>> getWeekAvailability(Long doctorId, LocalDate startDate) {
        List<Map<String, Object>> days = new ArrayList<>();

        // ★ Hafta aralığındaki izin günlerini toplu çek (performans)
        LocalDate endDate = startDate.plusDays(6);
        List<DoctorDayOff> weekDayOffs = dayOffRepository.findByDoctorIdAndOffDateBetween(doctorId, startDate, endDate);
        Set<LocalDate> dayOffDates = weekDayOffs.stream()
                .map(DoctorDayOff::getOffDate)
                .collect(Collectors.toSet());

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            Map<String, Object> day = new LinkedHashMap<>();
            day.put("date", date.toString());
            day.put("dayOfWeek", date.getDayOfWeek().name());
            day.put("dayLabel", getDayLabel(date.getDayOfWeek()));

            // ★ İzin günü mü?
            if (dayOffDates.contains(date)) {
                DoctorDayOff dayOff = weekDayOffs.stream()
                        .filter(d -> d.getOffDate().equals(date)).findFirst().orElse(null);
                day.put("available", false);
                day.put("totalSlots", 0);
                day.put("availableSlots", 0);
                day.put("status", "DAY_OFF");
                day.put("dayOffReason", dayOff != null && dayOff.getReason() != null ? dayOff.getReason() : "İzinli");
                days.add(day);
                continue;
            }

            DoctorSchedule schedule = scheduleRepository
                    .findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek())
                    .orElse(null);

            if (schedule == null || !schedule.isAvailable()) {
                day.put("available", false);
                day.put("totalSlots", 0);
                day.put("availableSlots", 0);
                day.put("status", "CLOSED"); // Kapalı gün
            } else {
                List<AvailableSlotDTO> slots = getAvailableSlots(doctorId, date);

                // Toplam slot sayısını hesapla
                int totalSlots = 0;
                LocalTime t = schedule.getStartTime();
                while (t.plusMinutes(schedule.getSlotDurationMinutes()).compareTo(schedule.getEndTime()) <= 0) {
                    totalSlots++;
                    t = t.plusMinutes(schedule.getSlotDurationMinutes());
                }

                day.put("available", true);
                day.put("totalSlots", totalSlots);
                day.put("availableSlots", slots.size());

                if (slots.isEmpty()) day.put("status", "FULL"); // Tamamen dolu
                else if (slots.size() <= 2) day.put("status", "ALMOST_FULL"); // Neredeyse dolu
                else day.put("status", "AVAILABLE"); // Müsait
            }

            days.add(day);
        }
        return days;
    }

    // =================== İZİN GÜNÜ YÖNETİMİ ===================

    /**
     * Doktor izin günü ekle
     */
    public Map<String, Object> addDayOff(String doctorEmail, LocalDate offDate, String reason) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        // Geçmiş tarih kontrolü
        if (offDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Geçmiş bir tarih için izin eklenemez.");
        }

        // Zaten var mı?
        if (dayOffRepository.existsByDoctorIdAndOffDate(doctor.getId(), offDate)) {
            throw new IllegalArgumentException("Bu tarih için zaten izin kaydı mevcut.");
        }

        DoctorDayOff dayOff = new DoctorDayOff();
        dayOff.setDoctor(doctor);
        dayOff.setOffDate(offDate);
        dayOff.setReason(reason);
        dayOffRepository.save(dayOff);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("date", offDate.toString());
        result.put("dayLabel", getDayLabel(offDate.getDayOfWeek()));
        result.put("reason", reason);
        result.put("message", offDate + " tarihinde izin kaydedildi.");
        return result;
    }

    /**
     * Doktor izin günü kaldır
     */
    public Map<String, Object> removeDayOff(String doctorEmail, LocalDate offDate) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        DoctorDayOff dayOff = dayOffRepository.findByDoctorIdAndOffDate(doctor.getId(), offDate)
                .orElseThrow(() -> new IllegalArgumentException("Bu tarihte izin kaydı bulunamadı."));

        dayOffRepository.delete(dayOff);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("message", offDate + " tarihindeki izin kaldırıldı.");
        return result;
    }

    /**
     * Doktorun gelecek izin günlerini listele
     */
    public List<Map<String, Object>> getDayOffs(String doctorEmail) {
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new UserNotFoundException("Doktor bulunamadı"));

        List<DoctorDayOff> dayOffs = dayOffRepository
                .findByDoctorIdAndOffDateGreaterThanEqualOrderByOffDateAsc(doctor.getId(), LocalDate.now());

        return dayOffs.stream().map(d -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", d.getId());
            m.put("date", d.getOffDate().toString());
            m.put("dayOfWeek", d.getOffDate().getDayOfWeek().name());
            m.put("dayLabel", getDayLabel(d.getOffDate().getDayOfWeek()));
            m.put("reason", d.getReason());
            return m;
        }).collect(Collectors.toList());
    }

    private String getDayLabel(DayOfWeek dow) {
        return switch (dow) {
            case MONDAY -> "Pazartesi";
            case TUESDAY -> "Salı";
            case WEDNESDAY -> "Çarşamba";
            case THURSDAY -> "Perşembe";
            case FRIDAY -> "Cuma";
            case SATURDAY -> "Cumartesi";
            case SUNDAY -> "Pazar";
        };
    }

    // Yardımcı: Slot süresini belirle
    private int getSlotDuration(Long doctorId, LocalDateTime dateTime) {
        DayOfWeek dow = dateTime.getDayOfWeek();
        return scheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dow)
                .map(DoctorSchedule::getSlotDurationMinutes)
                .orElse(30);
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
