package com.asdf.todo.controller;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.entity.Todo;
import com.asdf.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTests {

    @Autowired private MockMvc mockMvc;

    // @MockBean은 이제 사용되지 않는다.
    @MockitoBean private TodoService todoService;

    // ID로 Todo 단일 조회하는 API 동작 검증
    // GET /api/todos/v1/{id} 테스트
    @Test
    public void testGetTodoById() throws Exception{
        // 테스트용 데이터 생성
        TodoResponseDto todo = new TodoResponseDto(1L, "Test Todo", "Description", false);

        // 서비스 Mock 설정
        // 진짜 로직을 실행하지 않고 이 todo 반환
        given(todoService.findById(1L)).willReturn(todo);

        // MockMvc는 실제 서버 없이 가짜 HTTP 요청을 보낸다
        // API 호출 시뮬레이션
        mockMvc.perform(get("/api/todos/v2/1")
                .accept(MediaType.APPLICATION_JSON)) // Json 요청 명시
                .andExpect(status().isOk()) // 상태 코드 검증 (200 OK)
                .andExpect(jsonPath("$.id").value(1L)) // 응답 body 검증 (id)
                .andExpect(jsonPath("$.title").value("Test Todo")); // 응답 body 검증 (title)
    }

    // 전체 Todo 조회 API 테스트
    //  Get /api/todos/v1 에 데이터 있을 떄, 없을 떄 검증
    @Test
    public void testGetAllTodos() throws Exception{
        // 데이터 없는 경우 Mock 설정
        // Service가 빈 리스트를 반환하도록 설정 -> []
        given(todoService.findAll()).willReturn(Collections.emptyList());

        // 전체 조회 요청 실행
        // 가짜 HTTP 요청
        mockMvc.perform(get("/api/todos/v2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // 응답 상태 검증

        // 데이터 있는 경우 Mock 설정
        given(todoService.findAll())
                .willReturn(
                        Collections.singletonList(
                                new TodoResponseDto(1L, "Test Todo", "Description", false)));

        // 전체 조회 요청 실행
        mockMvc.perform(get("/api/todos/v2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 상태 코드 검증 (200 OK)
                .andExpect(jsonPath("$[0].id").value(1L)) // 첫 번째 요소($[0]) id(.id) 검증
                .andExpect(jsonPath("$[0].title").value("Test Todo")); // 첫 번째 요소 title 검증
    }

    // Todo 생성 API 테스트
    // Post /api/todos/v1 가 정상적으로 새 Todo를 만드는지 검증
    @Test
    public void testCreateTodo() throws Exception{
        // 생성 후 반환될 Todo 준비
        TodoResponseDto todo = new TodoResponseDto(1L, "New Todo", "Description", false);

        // Service Mock 설정
        // 어떤 Todo가 들어와도 저장하면 todo를 반환
        given(todoService.save(any(TodoRequestDto.class))).willReturn(todo);

        // POST 요청 실행
        // 가짜 HTTP POST 요청
        mockMvc.perform(
                post("/api/todos/v2")
                        .contentType(MediaType.APPLICATION_JSON) // JSON 요청 타입 지정
                        .content("{\"title\": \"New Todo\", \"description\": " + "\"Description\"}")) // 요청 body 전달
                .andExpect(status().isCreated()) // 상태 코드 검증 (201 Created)
                .andExpect(jsonPath("$.id").value(1L)) // 응답 JSON id 검증
                .andExpect(jsonPath("$.title").value("New Todo")); // 응답 JSON title 검증
    }

    // Todo 수정 API 테스트
    // PUT /api/todos/v1/{id}
    @Test
    public void testUpdateTodo() throws Exception{
        // 기존 데이터 생성 (수정 전 데이터)
        TodoResponseDto existingTodo = new TodoResponseDto(1L, "Existion Todo", "Description", false);

        // 수정 후 데이터 생성
        TodoResponseDto updatedTodo = new TodoResponseDto(1L, "Updated Todo", "Updated Description", true);

        // findById Mock 설정
        // Controller가 수정 전 존재 여부 확인할 때 existingTodo 반환
        given(todoService.findById(1L)).willReturn(existingTodo);

        // Update Mock 설정
        // 어떤 id와 어떤 Todo가 와도 updateTodo 반환
        given(todoService.update(anyLong(), any(TodoRequestDto.class)))
                .willReturn(updatedTodo);

        // PUT 요청 실행
        mockMvc.perform(
                put("/api/todos/v2/1")
                        .contentType(MediaType.APPLICATION_JSON) // JSON 타입 지정
                        .content("{\"title\":  \"Updated Todo\", \"description\": \"Updated" + "Description\"}")) // 수정 요청 body
                .andExpect(status().isOk()) // 상태 코드 검증 (200 OK)
                .andExpect(jsonPath("$.id").value(1L)) // id 검증
                .andExpect(jsonPath("$.title").value("Updated Todo")); // title 검증
    }


    // Todo 삭제 API 테스트
    // Delete /api/todos/v1/{id}
    @Test
    public void testDeleteTodo() throws Exception{
        // 삭제 대상 Todo 생성
        TodoResponseDto todo = new TodoResponseDto(1L, "Test Todo", "Description", false);

        // 존재 여부 Mock 설정 (존재할 때 todo 반환)
        given(todoService.findById(1L)).willReturn(todo);

        // Delete 요청 실행
        mockMvc.perform(delete("/api/todos/v2/1")
                        .accept(MediaType.APPLICATION_JSON)) // JSON 응답 요청
                .andExpect(status().isNoContent()); // 상태 코드 검증 (204 No Content)
    }
}
