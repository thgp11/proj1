package com.hospital.controller;

import com.hospital.dto.ReservationRequestDTO;
import com.hospital.entity.Reservation;
import com.hospital.repository.MemberRepository;
import com.hospital.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final MemberRepository memberRepository;

    // 예약 생성
    @PostMapping("/create")
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequestDTO dto, Authentication authentication){
        String email = authentication.getName(); // 인증된 사용자 email
        Reservation reservation = reservationService.createReservation(email, dto);
        return ResponseEntity.ok(reservation);
    }

    // 예약 조회(환자용)
    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> getMyReservations(Authentication auth){
        String email = auth.getName();
        List<Reservation> reservations = reservationService.getReservationsByMemberEmail(email);

        return ResponseEntity.ok(reservations);
    }

    // 예약 조회(의사용)
    @GetMapping("/doctor")
    public ResponseEntity<List<Reservation>> getDoctorReservations(Authentication auth){
        String email = auth.getName();
        List<Reservation> reservations = reservationService.getReservationsByDoctorEmail(email);

        return ResponseEntity.ok(reservations);
    }

    // 예약 취소
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable("id") Long reservationId, Authentication auth){
        String email = auth.getName();
        Reservation canceled = reservationService.cancelReservation(reservationId, email);

        return ResponseEntity.ok(canceled);
    }
}
