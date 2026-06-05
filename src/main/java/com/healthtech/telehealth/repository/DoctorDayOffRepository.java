package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.DoctorDayOff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorDayOffRepository extends JpaRepository<DoctorDayOff, Long> {

    // Doktorun belirli tarihteki izni
    Optional<DoctorDayOff> findByDoctorIdAndOffDate(Long doctorId, LocalDate offDate);

    // Doktorun belirli tarih aralığındaki izinleri
    List<DoctorDayOff> findByDoctorIdAndOffDateBetween(Long doctorId, LocalDate start, LocalDate end);

    // Doktorun tüm izinleri (gelecek tarihler)
    List<DoctorDayOff> findByDoctorIdAndOffDateGreaterThanEqualOrderByOffDateAsc(Long doctorId, LocalDate fromDate);

    // Doktorun belirli tarihte izni var mı?
    boolean existsByDoctorIdAndOffDate(Long doctorId, LocalDate offDate);
}
