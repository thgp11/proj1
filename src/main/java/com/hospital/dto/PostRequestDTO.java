package com.hospital.dto;

import com.hospital.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {
    @NotNull(message = "비회원은 글을 작성할 수 없습니다.")
    private Long memberId;
    @NotNull(message = "카테고리를 설정해주세요.")
    private Long categoryId;
    @NotBlank(message = "제목을 작성해주세요.")
    private String title;
    @NotBlank(message = "내용을 작성해주세요.")
    private String content;
}
