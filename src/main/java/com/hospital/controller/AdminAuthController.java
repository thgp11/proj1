package com.hospital.controller;

import com.hospital.dto.DoctorRequestDTO;
import com.hospital.dto.SignupRequestDTO;
import com.hospital.entity.DoctorRequest;
import com.hospital.entity.MemberRole;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.DoctorRequestRepository;
import com.hospital.repository.MemberRepository;
import com.hospital.service.AdminService;
import com.hospital.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final MemberService memberService;
    private final DoctorRequestRepository doctorRequestRepository;
    private final MemberRepository memberRepository;
    private final DoctorRepository doctorRepository;
    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<?> adminSignup(@RequestBody SignupRequestDTO request){
        if(request.getRole() != MemberRole.ADMIN) {
            return ResponseEntity.badRequest().body("관리자 권한으로만 가입 가능");
        }

        memberService.signupAdmin(request);
        return ResponseEntity.ok("관리자 계정 생성 완료");
    }

    @GetMapping("/doctor-requests")
    public ResponseEntity<List<DoctorRequestDTO>> getDoctorRequests() {
        List<DoctorRequest> requests = doctorRequestRepository.findByApprovedFalse();
        List<DoctorRequestDTO> dto = requests.stream()
                .map(DoctorRequestDTO::from)
                .toList();

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/doctor-requests/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        adminService.approveDoctorRequest(id);

        return ResponseEntity.ok("의사 등록 완료");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/doctor-requests/{id}/reject")
    public ResponseEntity<?> rejectDoctorRequest(@PathVariable Long id) {
        DoctorRequest request = doctorRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청이 존재하지 않습니다."));

        if (request.isApproved() || request.isRejected()) {
            return ResponseEntity.badRequest().body("이미 처리된 요청입니다.");
        }

        request.setRejected(true);
        doctorRequestRepository.save(request);
        return ResponseEntity.ok("의사 등록 요청이 거절되었습니다.");
    }
}
