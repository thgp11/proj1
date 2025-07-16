package com.hospital.repository;

import com.hospital.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r JOIN FETCH r.doctor WHERE r.member.email = :email")
    List<Reservation> getReservationsByMemberEmail(@Param("email") String email);

    List<Reservation> findByDoctorId(Long doctorId);
    List<Reservation> findByMemberId(Long MemberId);
    Reservation findByTimeSlotId(Long timeSlotId);

    boolean existsByMemberIdAndTimeSlotId(Long memberId, Long timeSlotId);

}
