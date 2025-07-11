package com.hospital.dto;

import com.hospital.entity.ReservationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationRequestDTO {
    private Long doctorId; // 의사 ID
    private Long timeSlotId; // 시간대 ID
    private String symptoms; // 증상 설명
    private ReservationStatus status; // 예약 상태 (예: "예약됨", "취소됨" 등)
}
