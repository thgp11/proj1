package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO{
    private String id;
    private String password;
    private String email;
    private String name;
//    String phoneNumber,
//    String address,
//    String role // e.g., "patient", "doctor", "admin"

}
