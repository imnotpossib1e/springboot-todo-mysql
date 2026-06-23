package com.asdf.todo.controller;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST 컨트롤러 빈 선언
// HTTP 메소드의 공통된 URL 지정
@RestController
@RequestMapping("/api/todos/v2")
public class TodoController {
    @Autowired
    private TodoService todoService;

    // 모든 항목 조회 API
    // Todo를 가져와서 있으면 200, 없으면 204 반환
    @GetMapping
    @Operation(summary = "전체 작업 조회", description = "전체 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "204", description = "내용 없음")
    })
    public ResponseEntity<List<TodoResponseDto>> getAllTodos() { // 여러개(List) 응답
        List<TodoResponseDto> todos = todoService.findAll(); // Service 호출

        // 빈 목록 체크
        if (todos.isEmpty()) {
            return ResponseEntity.noContent().build(); // body 없음 (204 반환)
        }
        return ResponseEntity.ok(todos); // 200 OK
    }

    // 특정 ID Todo 항목 조회
    // URL로 받은 id를 이용해 Todo 조회, 있으면 200, 없으면 404 응답
    @GetMapping("/{id}")
    @Operation(summary = "작업 조회", description = "ID로 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) { // 응답 데이터와 상태 코드 같이 반환
        TodoResponseDto todo = todoService.findById(id); // Service 호출

        if (todo == null) {
            return ResponseEntity.notFound().build(); // body 없음 (404 not found)
        }

        return ResponseEntity.ok(todo); // 200 OK
    }

    // Todo 항목 생성
    // 클라이언트가 보낸 Todo데이터를 받아 DB에 저장, 생성된 결과 201 상태로 반환
    @PostMapping
    @Operation(summary = "작업 생성", description = "새로운 작업 생성")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "생성됨")})
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto todo) { // @RequestBody: 요청 body(JSON)을 Java 객체로 변환
        return ResponseEntity.status(201).body(todoService.save(todo)); // 서비스 호출하여 응답 생성
    }

    // 기존 Todo 항목 수정
    // URL의 id에 해당하는 Todo가 존재하면 수정하고, 없으면 404 반환
    @PutMapping("/{id}")
    @Operation(summary = "작업 수정", description = "ID로 작업 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto todo) {
        TodoResponseDto existingTodo = todoService.findById(id); // 기존 데이터 조회
        if (existingTodo == null) {
            return ResponseEntity.notFound().build(); // 없으면 404
        }
        return ResponseEntity.ok(todoService.update(id, todo)); // 수정 실행
    }

    // 특정 ID의 Todo 항목 삭제
    // id로 Todo를 찾아 존재하면 삭제하고 204 반환, 없으면 404 반환
    @DeleteMapping("/{id}")
    @Operation(summary = "작업 삭제", description = "ID로 작업 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "내용 없음"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        TodoResponseDto todo = todoService.findById(id); // 기존 데이터 조회

        if (todo == null) {
            return ResponseEntity.notFound().build(); // 없으면 404
        }

        todoService.delete(id); // 삭제 실행
        return ResponseEntity.noContent().build(); // 성공 응답
    }
}
