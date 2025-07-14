package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorRequest;
import com.hospital.entity.Member;
import com.hospital.entity.MemberRole;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.DoctorRequestRepository;
import com.hospital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DoctorRequestRepository doctorRequestRepository;
    private final MemberRepository memberRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public void approveDoctorRequest(Long requestId) {
        DoctorRequest request = doctorRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 요청이 존재하지 않습니다."));

        Member member = request.getMember();
        member.setRole(MemberRole.DOCTOR);
        memberRepository.save(member);

        Doctor doctor = Doctor.builder()
                .member(member)
                .department(request.getDepartment())
                .doctorNumber(request.getDoctorNumber())
                .build();
        doctorRepository.save(doctor);

        request.setApproved(true);
        doctorRequestRepository.save(request);
    }
}
