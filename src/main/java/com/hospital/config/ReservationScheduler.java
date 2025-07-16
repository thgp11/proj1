package com.hospital.config;

import com.hospital.entity.Reservation;
import com.hospital.entity.ReservationStatus;
import com.hospital.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;

    // 예약 상태를 주기적으로 확인하고 필요한 작업을 수행하는 메소드
    @Scheduled(cron = "0 */10 9-18 * * MON-FRI") // 평일 9~18시, 매 10분마다 실행
    public void autoCancelExpiredReservations() {
        log.info("자동 예약 취소 스케줄러 실행");

        List<Reservation> reservations = reservationRepository.findAll();

        // 현재 시간보다 이전인 예약을 찾아 취소 처리
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.RESERVED &&
                    reservation.getTimeSlot().getEndTime().isBefore(LocalDateTime.now())) {

                reservation.setStatus(ReservationStatus.CANCELED);
                reservation.getTimeSlot().setAvailable(true);
                reservationRepository.save(reservation);

                log.info("예약 ID {} 자동 취소 완료", reservation.getId());
            }
        }
        log.info("자동 예약 취소 작업 완료");
    }
}