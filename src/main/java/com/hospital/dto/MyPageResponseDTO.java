package com.hospital.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponseDTO {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
    private String department;
    private String doctorNumber;
}
