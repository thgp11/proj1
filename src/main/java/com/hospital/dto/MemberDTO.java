package com.hospital.dto;

import com.hospital.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    public static MemberDTO from(Member member) {
        return MemberDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .build();
    }
}
