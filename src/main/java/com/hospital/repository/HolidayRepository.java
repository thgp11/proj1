package com.hospital.repository;

import com.hospital.entity.Doctor;
import com.hospital.entity.Holiday;
import com.hospital.entity.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    // 특정 의사의 휴일을 조회하는 메서드
    List<Holiday> findByDoctor(Doctor doctor);
    List<Holiday> findByDoctorId(Long doctorId);

    // 특정 날짜에 해당 의사가 휴일인지 확인하는 메서드
    boolean existsByDoctorIdAndDateAndType(Long doctorId, LocalDate date, HolidayType type);

    // 특정 의사의 휴일을 날짜로 조회하는 메서드
    List<Holiday> findByDoctorIdAndDate(Long doctorId, LocalDate date);

    // 특정 의사의 휴일을 날짜 범위로 조회하는 메서드
    List<Holiday> findByDoctorIdAndDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);

    // 특정 날짜가 어떤 타입의 휴일인지 확인하는 메서드
    boolean existsByDateAndType(LocalDate date, HolidayType type);
}
