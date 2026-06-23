package com.asdf.todo.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class TodoRequestDto {
    // @NotBlank는 API 요청 검증이 가능하다.
    @NonNull
    private String title;
    @NonNull private String description;
    @Builder.Default
    private boolean completed = false;

    // 생성자 1
    // 모든 값을 직접 넣는 경우
    public TodoRequestDto(String title, String description, boolean completed){
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // 생성자 2
    // completed 생략 가능, 자동으로 false 값 넣음
    public TodoRequestDto(String title, String description){
        this(title, description, false);
    }

}
