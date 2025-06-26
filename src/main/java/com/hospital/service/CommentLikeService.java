package com.hospital.service;

import com.hospital.dto.CommentLikeRequestDTO;
import com.hospital.entity.Comment;
import com.hospital.entity.CommentLike;
import com.hospital.entity.Post;
import com.hospital.entity.Member;
import com.hospital.repository.CommentLikeRepository;
import com.hospital.repository.CommentRepository;
import com.hospital.repository.MemberRepository;
import com.hospital.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public String likeComment(CommentLikeRequestDTO dto) {

        // 1. 이미 좋아요를 눌렀는지 확인하는 로직
        if (commentLikeRepository.existsByComment_IdAndMember_Id(dto.getCommentId(), dto.getMemberId())) {
            throw new IllegalArgumentException("이미 추천한 댓글입니다.");
        }

        // 2. 좋아요 대상 가져오기
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        // 3. 좋아요를 누른 회원 객체 가져오기
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(()-> new NoSuchElementException("추천은 회원만 가능합니다."));

        // 4. CommentLike 엔터티 저장
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();

        commentLikeRepository.save(commentLike);
        return "댓글이 추천되었습니다.";
    }
}
