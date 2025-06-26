package com.hospital.service;

import com.hospital.dto.PostRequestDTO;
import com.hospital.entity.Category;
import com.hospital.entity.Member;
import com.hospital.entity.Post;
import com.hospital.repository.CategoryRepository;
import com.hospital.repository.MemberRepository;
import com.hospital.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostRequestService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public String CreatePost(PostRequestDTO dto) {
        // 1. 글 작성자의 회원 여부 확인
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("글 작성은 회원만 가능합니다."));
        // 2. 카테고리 조회
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("카테고리를 선택해주세요."));

        // 3. 비속어 확인

        // 4. post 엔터티 생성
        Post post = Post.builder()
                .category(category)
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        // 5. Post 엔터티 저장
        postRepository.save(post);
        return "게시글 작성 완료";
    }
}
