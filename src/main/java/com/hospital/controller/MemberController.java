package com.hospital.controller;

import com.hospital.dto.DoctorRequestDTO;
import com.hospital.dto.MyPageResponseDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorRequest;
import com.hospital.entity.Member;
import com.hospital.entity.MemberRole;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.DoctorRequestRepository;
import com.hospital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberRepository memberRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorRequestRepository doctorRequestRepository;


    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDTO> getMyPage(Authentication auth) {
        String email = auth.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        MyPageResponseDTO.MyPageResponseDTOBuilder dtoBuilder = MyPageResponseDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole().name())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber());

        if (member.getRole() == MemberRole.DOCTOR) {
            Doctor doctor = doctorRepository.findByMember(member)
                    .orElseThrow(() -> new IllegalStateException("의사 정보 없음"));
            dtoBuilder.department(doctor.getDepartment())
                    .doctorNumber(doctor.getDoctorNumber());
        }

        return ResponseEntity.ok(dtoBuilder.build());
    }

    @GetMapping("/doctor/request")
    public ResponseEntity<?> requestDoctor(@RequestBody DoctorRequestDTO dto, Authentication auth) {
        String email = auth.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow();

        if (doctorRequestRepository.existsByMember(member)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("이미 등록 요청을 보냈습니다.");
        }

        DoctorRequest request = DoctorRequest.builder()
                .member(member)
                .department(dto.getDepartment())
                .doctorNumber(dto.getDoctorNumber())
                .build();

        doctorRequestRepository.save(request);
        return ResponseEntity.ok("의사 등록 요청 완료");
    }
}
