package com.hospital.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationResponseDTO {
    private Long id; // 예약 ID
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String symptoms;
    private String memberName;
    private String doctorName;
}
