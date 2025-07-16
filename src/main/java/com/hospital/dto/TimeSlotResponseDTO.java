package com.hospital.dto;

import com.hospital.entity.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponseDTO {
    private Long id;
    private String timeText; // e.g., "09:00 ~ 10:00"
    private boolean available;

    // 정적 팩토리 메서드 : TimeSlot 엔티티를 TimeSlotResponseDTO로 변환
    public static TimeSlotResponseDTO fromEntity(TimeSlot timeSlot){
        String formattedTime = formatTimeRange(timeSlot);
        return new TimeSlotResponseDTO(
                timeSlot.getId(),
                formattedTime,
                timeSlot.isAvailable()
        );
    }

    // 시간 포매팅
    public static String formatTimeRange (TimeSlot slot) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return slot.getStartTime().format(formatter) + " ~ " + slot.getEndTime().format(formatter);
    }
}
