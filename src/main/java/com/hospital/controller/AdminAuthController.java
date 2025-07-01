package com.hospital.controller;

import com.hospital.dto.SignupRequestDTO;
import com.hospital.entity.MemberRole;
import com.hospital.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminAuthController {

    private final MemberService memberService;

    public AdminAuthController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> adminSignup(@RequestBody SignupRequestDTO request){
        if(request.getRole() != MemberRole.ADMIN) {
            return ResponseEntity.badRequest().body("관리자 권한으로만 가입 가능");
        }

        memberService.signupAdmin(request);
        return ResponseEntity.ok("관리자 계정 생성 완료");
    }
}
