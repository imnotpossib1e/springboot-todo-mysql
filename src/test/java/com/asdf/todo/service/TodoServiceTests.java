package com.asdf.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TodoServiceTests {

//    @Container
//    public static MySQLContainer<?> mySQLContainer =
//            new MySQLContainer<>("mysql:8.0")
//                    .withDatabaseName("testdb")
//                    .withUsername("test")
//                    .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry){
//        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mySQLContainer::getUsername);
//        registry.add("spring.datasource.password", mySQLContainer::getPassword);
//        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
//    }

    // Spring이 TodoService를 자동 주입
    @Autowired private TodoService todoService;

    @Autowired private TodoRepository todoRepository;

    private Long todo1Id;
    private Long todo2Id;

    // 각 테스트 메서드 실행 전에 항상 실행
    // 두개의 테스트 데이터 저장 (테스트는 항상 같은 시작 상태여야 하기 때문에 BeforeEach로 초기화 -> 고정 데이터)
    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
        todo1Id = todoService.save(new TodoRequestDto("Test Todo 1", "Description 1")).getId();
        todo2Id = todoService.save(new TodoRequestDto("Test Todo 2", "Description 2")).getId();
    }

    // 전체 조회 테스트
    @Test
    void testFindAll() {
        // 전체 조회 실행
        List<TodoResponseDto> todos = todoService.findAll();
        // 검증 -> todos 리스트의 크기가 2여야 한다.
        assertThat(todos).hasSize(2);
        
    }

    // 저장 테스트
    // 새로운 Todo를 저장하면 전체 개수가 하나 증가하는지 확인
    @Test
    void testSaveTodo(){
        // 새로운 Todo 생성
        TodoRequestDto todoRequestDto = new TodoRequestDto("New Todo", "New Description");
        // 저장 실행
        todoService.save(todoRequestDto);
        // 검증 -> 전체 todo 개수가 3이어야 한다.
        assertThat(todoService.findAll()).hasSize(3);
    }

    // 단일 ID 조회 테스트
    // 1번을 찾으면 제대로 꺼내오는지
    @Test
    void testFindById() {
        // ID 조회
        TodoResponseDto todo = todoService.findById(todo1Id);
        // 값 존재 검증
        assertThat(todo).isNotNull();
        // title 검증
        assertThat(todo.getTitle()).isEqualTo("Test Todo 1");
    }

    // Todo 수정 테스트
    // 기존 todo를 수정하면 실제로 값이 바뀌어서 저장되는지 확인
    @Test
    void testUpdateTodo() {
        // 수정할 Todo 데이터 생성
        TodoRequestDto updatedTodo = new TodoRequestDto("Updated Todo", "Updated Description", true);
        // update 실행
        todoService.update(todo1Id, updatedTodo);
        // Todo 다시 조회하여 수정되었는지 확인
        TodoResponseDto todo = todoService.findById(todo1Id);
        // title 검증
        assertThat(todo.getTitle()).isEqualTo("Updated Todo");
        // description 검증
        assertThat(todo.getDescription()).isEqualTo("Updated Description");
        // completed 검증 (false -> true로 변경되었는지)
        assertThat(todo.isCompleted()).isTrue();
    }

    // 삭제 테스트
    @Test
    void testDeleteTodo() {
        // 삭제 실행
        todoService.delete(todo1Id);
        // 전체 개수 검증 (1개만 남아야 함)
        assertThat(todoService.findAll()).hasSize(1);
        // 특정 ID 검증
        assertThat(todoService.findById(todo1Id)).isNull();
    }
}
