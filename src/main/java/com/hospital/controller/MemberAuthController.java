package com.hospital.controller;

import com.hospital.dto.LoginRequestDTO;
import com.hospital.dto.SignupRequestDTO;
import com.hospital.service.MemberService;
import com.hospital.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class MemberAuthController {
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private final MemberService memberService;

    public MemberAuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MemberService memberService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            UserDetails user = (UserDetails) auth.getPrincipal();
            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            String token = jwtUtil.generateToken(user.getUsername(), roles);

            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO dto) {
        try {
            memberService.signup(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입에 실패했습니다: " + e.getMessage());
        }

    }

    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestBody String email) {
        boolean exists = memberService.existsByEmail(email);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 로그아웃 로직은 클라이언트 측에서 토큰을 삭제하는 것으로 처리
        return ResponseEntity.ok("로그아웃되었습니다. (클라이언트에서 토큰 제거필요)");
    }

}
