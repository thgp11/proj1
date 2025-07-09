package com.hospital.service;

import com.hospital.dto.WeeklyRecurringTimeSlotRequestDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.HolidayType;
import com.hospital.entity.TimeSlot;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.HolidayRepository;
import com.hospital.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    private final HolidayRepository holidayRepository;

    // 반복 진료 시간 슬롯 생성

    public List<TimeSlot> generateWeeklyRecurringSlots(WeeklyRecurringTimeSlotRequestDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new NoSuchElementException("해당 의사를 찾을 수 없습니다."));

        if (dto.getStartDate().plusMonths(2).isBefore(dto.getEndDate())) {
            throw new IllegalArgumentException("최대 2개월까지만 등록할 수 있습니다.");
        }

        Set<LocalDate> holidayRepository = new HashSet<>();
        holidayRepository.add(LocalDate.of(2025, 8, 15)); // 예시로 특정 날짜를 제외

        List<TimeSlot> slotList = new ArrayList<>();
        LocalDate currentDate = dto.getStartDate();
        LocalDateTime now = LocalDateTime.now();

        while (!currentDate.isAfter(dto.getEndDate())) {
            if (holidayRepository.contains(currentDate) || !dto.getDaysOfWeek().contains(currentDate.getDayOfWeek())) {
                currentDate = currentDate.plusDays(1);
                continue;
            }

            LocalTime currentTime = dto.getStartTime();
            while (currentTime.plusMinutes(dto.getDurationInMinutes()).isBefore(dto.getEndTime().plusSeconds(1))) {
                LocalDateTime slotStart = LocalDateTime.of(currentDate, currentTime);

                // 현재 시간보다 이전 시간은 건너뛰기
                if (slotStart.isBefore(now)) {
                    currentTime = currentTime.plusMinutes(dto.getDurationInMinutes());
                    continue;
                }

                slotList.add(TimeSlot.builder()
                        .doctor(doctor)
                        .startTime(slotStart)
                        .endTime(slotStart.plusMinutes(dto.getDurationInMinutes()))
                        .available(true)
                        .build());

                currentTime = currentTime.plusMinutes(dto.getDurationInMinutes());
            }
            currentDate = currentDate.plusDays(1);
        }
        return timeSlotRepository.saveAll(slotList);
    }

    // 의사 휴무일 여부 확인
    public boolean isHolidayForDoctor(LocalDate date, Doctor doctor) {
        Long doctorId = doctor.getId();
        boolean isDoctorSpecificHoliday = holidayRepository.existsByDoctorIdAndDateAndType(doctorId, date, HolidayType.DOCTOR_ABSENCE);
        boolean isNationalHoliday = holidayRepository.existsByDateAndType(date, HolidayType.NATIONAL);
        return isDoctorSpecificHoliday || isNationalHoliday;
    }

     // 의사의 예약 가능한 시간 슬롯 조회 (휴무일 제외)
    public List<TimeSlot> getAvailableSlots(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NoSuchElementException("의사를 찾을 수 없습니다."));

        LocalDateTime now = LocalDateTime.now();
        List<TimeSlot> availableSlots = timeSlotRepository
                .findByDoctorIdAndStartTimeAfterAndAvailableTrue(doctorId, now);

        return availableSlots.stream()
                .filter(slot -> !isHolidayForDoctor(slot.getStartTime().toLocalDate(), doctor))
                .collect(Collectors.toList());
    }

     // 예약 가능 시간 검증
    public void validateBookingTime(TimeSlot slot) {
        if (LocalDateTime.now().isAfter(slot.getStartTime())) {
            throw new IllegalArgumentException("지나간 시간에는 예약할 수 없습니다.");
        }
    }
}