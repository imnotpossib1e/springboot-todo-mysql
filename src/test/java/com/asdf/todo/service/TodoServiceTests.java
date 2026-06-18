package com.asdf.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TodoServiceTests {

    // Spring이 TodoService를 자동 주입
    @Autowired private TodoService todoService;

    // 각 테스트 메서드 실행 전에 항상 실행
    // 두개의 테스트 데이터 저장 (테스트는 항상 같은 시작 상태여야 하기 때문에 BeforeEach로 초기화 -> 고정 데이터)
    @BeforeEach
    void setUp() {
        // Service 직접 생성
        todoService = new TodoService(new TodoInMemoryRepository());
        // 테스트 데이터 저장 1
        todoService.save(new Todo(null, "Test Todo 1", "Description 1", false));
        // 테스트 데이터 저장 2
        todoService.save(new Todo(null, "Test Todo 2", "Description 2", true));
    }

    // 전체 조회 테스트
    @Test
    void testFindAll() {
        // 전체 조회 실행
        List<Todo> todos = todoService.findAll();
        // 검증 -> todos 리스트의 크기가 2여야 한다.
        assertThat(todos).hasSize(2);
        
    }

    // 저장 테스트
    // 새로운 Todo를 저장하면 전체 개수가 하나 증가하는지 확인
    @Test
    void testSaveTodo(){
        // 새로운 Todo 생성
        Todo todo = new Todo(null, "New Todo", "New Description", false);
        // 저장 실행
        Todo saved = todoService.save(todo);
        // 검증 -> 전체 todo 개수가 3이어야 한다.
        assertThat(todoService.findAll()).hasSize(3);

        // 저장값 추가 검증
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("New Todo");
    }

    // 단일 ID 조회 테스트
    // 1번을 찾으면 제대로 꺼내오는지
    @Test
    void testFindById() {
        // ID 조회
        Optional<Todo> todo = todoService.findById(1L);
        // 값 존재 검증
        assertThat(todo).isPresent();
        // title 검증
        assertThat(todo.get().getTitle()).isEqualTo("Test Todo 1");
    }

    // Todo 수정 테스트
    // 기존 todo를 수정하면 실제로 값이 바뀌어서 저장되는지 확인
    @Test
    void testUpdateTodo() {
        // 수정할 Todo 데이터 생성
        Todo updatedTodo = new Todo(1L, "Updated Todo", "Updated Description", true);
        // update 실행
        todoService.update(1L, updatedTodo);
        // Todo 다시 조회하여 수정되었는지 확인
        Optional<Todo> todo = todoService.findById(1L);
        // 존재 확인
        assertThat(todo).isPresent();
        // title 검증
        assertThat(todo.get().getTitle()).isEqualTo("Updated Todo");
        // description 검증
        assertThat(todo.get().getDescription()).isEqualTo("Updated Description");
        // completed 검증 (false -> true로 변경되었는지)
        assertThat(todo.get().isCompleted()).isTrue();
    }

    // 삭제 테스트
    @Test
    void testDeletedTodo() {
        // 삭제 실행
        todoService.delete(1L);
        // 전체 개수 검증 (1개만 남아야 함)
        assertThat(todoService.findAll()).hasSize(1);
        // 특정 ID 검증
        assertThat(todoService.findById(1L)).isEmpty();
    }
}
