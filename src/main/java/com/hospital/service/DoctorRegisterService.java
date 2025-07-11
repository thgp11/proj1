package com.hospital.service;

import com.hospital.dto.DoctorRegisterDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.Member;
import com.hospital.entity.MemberRole;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DoctorRegisterService {
    private final MemberRepository memberRepository;
    private final DoctorRepository doctorRepository;

    public Doctor registerDoctor(DoctorRegisterDTO dto, String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        if(doctorRepository.existsByMember(member)) {
            throw new IllegalStateException("이미 존재하는 의사 이메일입니다.");
        }

        member.setRole(MemberRole.DOCTOR);
        memberRepository.save(member);

        Doctor doctor = Doctor.builder()
                .member(member)
                .department(dto.getDepartment())
                .doctorNumber(dto.getDoctorNumber())
                .build();

        return doctorRepository.save(doctor);
    }
}
