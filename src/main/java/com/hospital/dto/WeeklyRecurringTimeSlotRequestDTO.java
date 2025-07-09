package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyRecurringTimeSlotRequestDTO {
    private Long doctorId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<java.time.DayOfWeek> daysOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private int durationInMinutes;
}
