package com.hospital.dto;

import com.hospital.entity.MemberRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequestDTO{
    private String id;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    // role은 일반 회원가입에서는 숨김처리하고 관리자용 api에서만 받아야함
    private MemberRole role;
}
