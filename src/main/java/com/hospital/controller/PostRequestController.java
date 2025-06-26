package com.hospital.controller;

import com.hospital.dto.PostRequestDTO;
import com.hospital.service.PostRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostRequestController {

    private final PostRequestService postRequestService;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostRequestDTO dto){
        String result = postRequestService.CreatePost(dto);
        return ResponseEntity.ok(result);
    }


}
