package com.asdf.todo.service;

import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Spring이 비즈니스 로직 계층 빈으로 관리
// Repository를 주입받아서 DB(인메모리)접근을 위임
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    /** 생성자 주입 Spring이 TodoInMemoryRepository를 자동 주입 final로 선언해서 불변성 확보 * */
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 전체 Todo 리스트 반환
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    // 단건 조회
    // 조회 결과가 없을 수 있는 가능성을 명시하게 위해 Optional<T> 타입으로 반환
    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    // 저장
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    // 수정
    // findByid의 Optional에 맞춰서 id조회, 없으면 예외 발생, 있으면 다음 코드 진행
    public Todo update(Long id, Todo todo) {
        todoRepository.findById(id).orElseThrow(() -> new RuntimeException("Todo not fount"));

        todo.setId(id);
        return todoRepository.save(todo);
    }

    // 삭제
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
}
