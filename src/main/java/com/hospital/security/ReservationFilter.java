package com.hospital.security;

import com.hospital.entity.TimeSlot;
import com.hospital.repository.TimeSlotRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationFilter {
    // 현재 이후 시간만 반환
    public List<TimeSlot> getAvailableSlots(Long doctorId, TimeSlotRepository timeSlotRepository) {
        LocalDateTime now = LocalDateTime.now();
        return timeSlotRepository.findByDoctorIdAndStartTimeAfterAndAvailableTrue(doctorId, now);
    }

    public void validateBookingTime(TimeSlot slot) {
        if (LocalDateTime.now().isAfter(slot.getStartTime())) {
            throw new IllegalArgumentException("지나간 시간에는 예약할 수 없습니다.");
        }
    }
}