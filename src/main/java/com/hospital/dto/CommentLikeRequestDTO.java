package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeRequestDTO {
    private Long commentId;
    private Long memberId;
    private Boolean likeStatus; // true for like, false for unlike
}
