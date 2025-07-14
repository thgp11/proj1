package com.hospital.repository;

import com.hospital.entity.Member;
import com.hospital.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByRole(MemberRole role);
    Optional<Member> findByEmail(String email);
}
