package com.asdf.todo.util;


import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.entity.Todo;

public class EntityDtoMapper {
    // DTO -> Entity
    // 클라이언트 요청 객체를 DB 저장 객체로 변환
    public static Todo toEntity(TodoRequestDto dto){
        return new Todo(
                null, // 자동 생성 필드 (id)
                dto.getTitle(),
                dto.getDescription(),
                dto.isCompleted(),
                null // 자동 생성 필드 (createAt)
        );
    }

    // Entity -> DTO
    // DB 객체를 응답 객체로 변환
    public static TodoResponseDto toDto(Todo entity){
        return new TodoResponseDto(
                entity.getId(), entity.getTitle(), entity.getDescription(), entity.isCompleted()
        );
    }
}
