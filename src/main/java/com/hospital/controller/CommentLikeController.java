package com.hospital.controller;

import com.hospital.dto.CommentLikeRequestDTO;
import com.hospital.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment-likes")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<String> likeComment(@RequestBody CommentLikeRequestDTO dto){
        String result = commentLikeService.likeComment(dto);
        return ResponseEntity.ok(result);
    }
}
