package com.hospital.service;

import com.hospital.dto.SignupRequestDTO;
import com.hospital.entity.Member;
import com.hospital.entity.MemberRole;
import com.hospital.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(SignupRequestDTO request) {
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
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member);
    }

    public void signupAdmin(SignupRequestDTO request){
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(MemberRole.ADMIN)
                .build();

        memberRepository.save(member);
    }
}
