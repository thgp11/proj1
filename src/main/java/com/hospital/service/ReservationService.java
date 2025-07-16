package com.hospital.service;

import com.hospital.dto.ReservationRequestDTO;
import com.hospital.entity.*;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.MemberRepository;
import com.hospital.repository.ReservationRepository;
import com.hospital.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DoctorRepository doctorRepository;
    private final MemberRepository memberRepository;
    private final TimeSlotRepository timeSlotRepository;

    public Reservation create(ReservationRequestDTO dto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new NoSuchElementException("의사 정보를 찾을 수 없습니다."));

        TimeSlot timeSlot = timeSlotRepository.findById(dto.getTimeSlotId())
                .orElseThrow(() -> new NoSuchElementException("예약 가능한 시간대가 없습니다."));

        if (LocalDateTime.now().isAfter(timeSlot.getStartTime())) {
            throw new IllegalArgumentException("지나간 시간에는 예약할 수 없습니다.");
        }

        if (!timeSlot.isAvailable()) {
            throw new IllegalStateException("선택하신 시간은 이미 예약되어있습니다.");
        }

        timeSlot.setAvailable(false); // 예약 불가로 변경

        Reservation reservation = Reservation.builder()
                .member(member)
                .doctor(doctor)
                .timeSlot(timeSlot)
                .date(LocalDate.now())
                .time(timeSlot.getStartTime().toLocalTime())
                .status(dto.getStatus())
                .symptoms(dto.getSymptoms())
                .createdAt(LocalDateTime.now())
                .build();

        timeSlotRepository.save(timeSlot); // 슬롯 상태 저장
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByMemberEmail(String email) {
        // 예약된 진료 확인 메서드
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
        return reservationRepository.findByMemberId(member.getId());
    }

    public List<Reservation> getReservationsByDoctorEmail(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("의사 계정이 존재하지 않습니다."));
        return reservationRepository.findByDoctorId(doctor.getId());
    }

    public Reservation cancelReservation (Long reservationId, String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다."));

        if (!reservation.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("본인의 예약만 취소할 수 있습니다.");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservation.getTimeSlot().setAvailable(true); // 시간 슬롯 다시 예약 가능으로

        timeSlotRepository.save(reservation.getTimeSlot());
        return reservationRepository.save(reservation);
    }
}
