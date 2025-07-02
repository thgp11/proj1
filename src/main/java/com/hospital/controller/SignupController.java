package com.hospital.controller;

import com.hospital.dto.SignupRequestDTO;
import com.hospital.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SignupController {

    private final MemberService memberService;

    public SignupController(MemberService memberService){
        this.memberService = memberService;
    }

    // 일반 회원가입 api
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO request){
        memberService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 관리자 전용 회원가입 api
    @PostMapping("/admin/signup")
    public ResponseEntity<String> signupAdmin(@RequestBody SignupRequestDTO request){
        memberService.signupAdmin(request);
        return ResponseEntity.ok("관리자 회원가입 성공");
    }
}
