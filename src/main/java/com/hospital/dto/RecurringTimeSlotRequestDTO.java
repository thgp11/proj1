package com.hospital.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class RecurringTimeSlotRequestDTO {
    private Long doctorId;

    private LocalDate startTime; // 반복 시작 날짜
    private LocalDate endTime; // 반복 종료 날짜

    private LocalTime startTimeOfDay; // 예약 시작 시간
    private LocalTime endTimeOfDay; // 예약 종료 시간

    private List<DayOfWeek> daysOfWeek; // 예약 요일 목록
    private int durationInMinutes; // 예약 시간 간격 (분 단위)
}
