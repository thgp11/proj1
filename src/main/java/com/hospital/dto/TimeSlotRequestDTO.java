package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotRequestDTO {
    private Long doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
