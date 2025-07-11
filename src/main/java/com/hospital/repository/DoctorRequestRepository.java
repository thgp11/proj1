package com.hospital.repository;

import com.hospital.entity.DoctorRequest;
import com.hospital.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long> {
    // 승인되지 않은 요청 목록
    List<DoctorRequest> findByApprovedFalse();

    // 특정 회원의 요청 조회
    Optional<DoctorRequest> findByMember(Member member);

    // 중복 요청 방지
    boolean existsByMember(Member member);
}
