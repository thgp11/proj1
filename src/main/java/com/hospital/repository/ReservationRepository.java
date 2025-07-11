package com.hospital.repository;

import com.hospital.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByDoctorId(Long doctorId);
    Reservation findByTimeSlotId(Long timeSlotId);

    boolean existsByMemberIdAndTimeSlotId(Long memberId, Long timeSlotId);

}
