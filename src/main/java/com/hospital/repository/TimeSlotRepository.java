package com.hospital.repository;

import com.hospital.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    // 특정 의사의 사용 가능한 시간 슬롯을 조회하는 메서드
    List<TimeSlot> findByDoctorIdAndStartTimeAfterAndAvailableTrue(
            Long doctorId, LocalDateTime startTime);

    // 시간대별 예약 가능 여부를 확인하는 메소드
    boolean existsByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);

    // 특정 날짜에 예약된 시간대를 조회하는 메소드
    List<TimeSlot> findByDate(LocalDate date);

    // 특정 의사의 특정 일자 모든 예약시간을 조회하는 메소드
    List<TimeSlot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
