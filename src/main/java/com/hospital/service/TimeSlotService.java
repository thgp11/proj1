package com.hospital.service;

import com.hospital.dto.ReservationRequestDTO;
import com.hospital.dto.WeeklyRecurringTimeSlotRequestDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.HolidayType;
import com.hospital.entity.Member;
import com.hospital.entity.TimeSlot;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.HolidayRepository;
import com.hospital.repository.MemberRepository;
import com.hospital.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
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
    private final MemberRepository memberRepository;
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

    // 헬퍼 메서드 생성
    private <T, ID> T findByIdOrThrow(JpaRepository<T, ID> repository, ID id, String errorMessage) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(errorMessage));
    }

    public void someReservationMethod(Long memberId, ReservationRequestDTO dto){
        // 예약 생성 로직
        // 1. 의사가 존재하는지 확인
        Doctor doctor = findByIdOrThrow(doctorRepository, dto.getDoctorId(), "의사 정보가 존재하지 않습니다.");
        // 2. 시간 슬롯이 유효한지 확인
        TimeSlot timeSlot = findByIdOrThrow(timeSlotRepository, dto.getTimeSlotId(), "예약을 찾을 수 없습니다.");
        validateBookingTime(timeSlot);

        // 3. 예약 생성 및 저장
        // 4. 예약된 시간 슬롯을 사용 불가능으로 설정

        // 예시 코드 (구현 필요)
    }
}