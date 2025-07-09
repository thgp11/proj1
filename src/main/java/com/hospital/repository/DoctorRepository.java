package com.hospital.repository;

import com.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean exitsByDoctorNumber(String doctorNumber);
    Optional<Doctor> findByDoctorNumber(String doctorNumber);
}
