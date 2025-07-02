package com.hospital.dto;

import com.hospital.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {

    @NotNull(message = "카테고리를 설정해주세요.")
    private Long categoryId;
    @NotBlank(message = "제목을 작성해주세요.")
    private String title;
    @NotBlank(message = "내용을 작성해주세요.")
    private String content;
}
