package com.hospital.dto;

import lombok.Data;

@Data
public class LoginRequest {
    public String email;
    public String password;
}
