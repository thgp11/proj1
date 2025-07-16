package com.hospital.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberInfoDTO {
    private Long id;
    private String name;
    private String email;
}
