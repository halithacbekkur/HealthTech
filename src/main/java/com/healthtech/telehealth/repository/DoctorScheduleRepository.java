package com.healthtech.telehealth.repository;

import com.healthtech.telehealth.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctorIdAndAvailableTrue(Long doctorId);
    List<DoctorSchedule> findByDoctorIdOrderByDayOfWeek(Long doctorId);
    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
}
