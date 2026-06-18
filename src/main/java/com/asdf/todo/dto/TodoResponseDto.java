package com.asdf.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponseDto {
    @NotNull
    private Long id; // 할 일의 ID
    @NotBlank private String title; // 할 일의 제목
    private String description; // 할 일에 대한 설명
    private boolean completed; // 할 일의 완료 여부
}
