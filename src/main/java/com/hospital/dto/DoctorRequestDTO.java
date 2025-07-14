package com.hospital.dto;

import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorRequest;
import com.hospital.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {
    private String department;
    private String doctorNumber;
    private MemberDTO member;

    public static DoctorRequestDTO from(DoctorRequest request) {
        return DoctorRequestDTO.builder()
                .department(request.getDepartment())
                .doctorNumber(request.getDoctorNumber())
                .member(MemberDTO.from(request.getMember()))
                .build();
    }

    public Doctor toDoctor (Member member) {
        return Doctor.builder()
                .member(member)
                .department(this.department)
                .doctorNumber(this.doctorNumber)
                .build();
    }
}
