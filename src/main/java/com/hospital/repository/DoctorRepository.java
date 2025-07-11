package com.hospital.repository;

import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorRequest;
import com.hospital.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByDoctorNumber(String doctorNumber);
    Optional<Doctor> findByDoctorNumber(String doctorNumber);
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByMember(Member member);
    boolean existsByMember(Member member);
}
