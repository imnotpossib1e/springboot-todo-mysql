package com.asdf.todo.controller;

import com.asdf.todo.entity.Todo;
import com.asdf.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/api/todos/v1")
public class TodoController {

    // 컨트롤러가 서비스 레이어의 메서드를 호출할 수 있도록 TodoService 빈을 컨트롤러 클래스에 주입
    @Autowired private TodoService todoService;

    // 모든 항목 조회 API
    @GetMapping
    @Operation(summary = "전체 작업 조회", description = "전체 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "204", description = "내용 없음")
    })
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.findAll();
        if (todos == null || todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(todos);
    }

    // 특정 ID Todo 항목 조회
    @GetMapping("/{id}")
    @Operation(summary = "작업 조회", description = "ID로 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.findById(id);

        if (todo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(todo.get());
    }

    // Todo 항목 생성
    // 생성이 성공하면 201 상태 코드를 명시적으로 설정하고 생성된 항목을 본문에 포함해 반환
    @PostMapping
    @Operation(summary = "작업 생성", description = "새로운 작업 생성")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "생성됨")})
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.status(201).body(todoService.save(todo));
    }

    // 기존 Todo 항목 수정
    // 해당하는 Todo 항목이 존재하지 않으면 HTTP 404 응답 반환
    @PutMapping("/{id}")
    @Operation(summary = "작업 수정", description = "ID로 작업 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Optional<Todo> existingTodo = todoService.findById(id);
        if (existingTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(todoService.update(id, todo));
    }

    // 특정 ID의 Todo 항목 삭제
    // 항목이 존재하면 delete 메서드 호출하여 항목 삭제하고 204 응답 반환
    // 황목이 존재하지 않으면 404 코드 반환
    @DeleteMapping("/{id}")
    @Operation(summary = "작업 삭제", description = "ID로 작업 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "내용 없음"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Todo> deleteTodo(@PathVariable Long id) {
        Optional<Todo> todo = todoService.findById(id);

        if (todo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
