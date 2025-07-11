package com.hospital.dto;

import com.hospital.entity.DoctorRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {
    private Long id;
    private String department;
    private String doctorNumber;
    private MemberDTO member;

    public static DoctorRequestDTO from(DoctorRequest request) {
        return DoctorRequestDTO.builder()
                .id(request.getId())
                .department(request.getDepartment())
                .doctorNumber(request.getDoctorNumber())
                .member(MemberDTO.from(request.getMember()))
                .build();
    }
}
