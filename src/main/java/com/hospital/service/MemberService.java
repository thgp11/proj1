package com.hospital.service;

import com.hospital.dto.SignupRequestDTO;
import com.hospital.entity.Member;
import com.hospital.entity.MemberRole;
import com.hospital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDTO request) {
        System.out.println("SignupRequestDTO: " + request);
        if (request.getEmail() == null || request.getPassword() == null || request.getName() == null) {
            throw new IllegalArgumentException("이메일, 비밀번호, 이름은 필수 입력값입니다.");
        }
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        if(request.getRole() != null && request.getRole() == MemberRole.ADMIN) {
            throw new AccessDeniedException("관리자 계정은 일반 가입으로 생성할 수 없습니다.");
        }
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member);
    }

    public void signupAdmin(SignupRequestDTO request){
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .role(MemberRole.ADMIN)
                .build();

        memberRepository.save(member);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isAdminExists() {
        return memberRepository.existsByRole(MemberRole.ADMIN);
    }
}
