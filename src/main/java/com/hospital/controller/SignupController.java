package com.hospital.controller;

import com.hospital.dto.SignupRequestDTO;
import com.hospital.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO requestDTO){
        userService.signup(requestDTO);
        return ResponseEntity.ok("Signup successful");
    }

}
