package com.hospital.controller;

import com.hospital.dto.SignupRequestDTO;
import com.hospital.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SignupController {

    private final MemberService memberService;

    public SignupController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO requestDTO){
        memberService.signup(requestDTO);
        return ResponseEntity.ok("Signup successful");
    }

}
