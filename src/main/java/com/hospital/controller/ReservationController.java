package com.hospital.controller;

import com.hospital.dto.ReservationRequestDTO;
import com.hospital.dto.ReservationResponseDTO;
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

    private ReservationResponseDTO convertToDTO(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .date(reservation.getDate())
                .time(reservation.getTimeSlot().getStartTime().toLocalTime())
                .status(reservation.getStatus().name()) // Enum → String
                .symptoms(reservation.getSymptoms())
                .memberName(reservation.getMember().getName())
                .doctorName(reservation.getDoctor().getName())
                .build();
    }

    // 예약 생성
    @PostMapping("/create")
    public ResponseEntity<ReservationResponseDTO> create(@RequestBody ReservationRequestDTO dto, Authentication auth){
        Reservation reservation = reservationService.create(dto, auth.getName());

        ReservationResponseDTO responseDTO = convertToDTO(reservation);

        return ResponseEntity.ok(responseDTO);
    }

    // 예약 조회(환자용)
    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponseDTO>> getMyReservations(Authentication auth){
        String email = auth.getName();
        List<Reservation> reservations = reservationService.getReservationsByMemberEmail(email);
        List<ReservationResponseDTO> dtoList = reservations.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
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
