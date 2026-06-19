package com.asdf.todo.service;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.asdf.todo.util.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Spring이 비즈니스 로직 계층 빈으로 관리
// Repository를 주입받아서 DB(인메모리)접근을 위임
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    // 생성자 주입
    // Spring이 TodoInMemoryRepository를 자동 주입 final로 선언해서 불변성 확보
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 전체 조회
    // DB에 있는 모든 Todo를 조최해서 Entity -> ResponseDto로 변환 후 리스트로 반환
    // 읽기 전용(조회)
    @Transactional(readOnly = true)
    public List<TodoResponseDto> findAll() {
        return todoRepository.findAll().stream() // 전체 조회(findAll()), 리스트 하나씩 처리 가능하게 만들어줌(stream())
                .map(EntityDtoMapper::toDto) // 각 Entity를 DTO로 변환
                .collect(Collectors.toList()); // 변환 결과를 List로 만듦
    }

    // 단건 조회
    // id로 Todo 하나를 조회해서 Entity를 DTO로 변환해서 반환하고, 없으면 null 반환
    @Transactional(readOnly = true)
    public TodoResponseDto findById(Long id) {
        return todoRepository.findById(id).
                map(EntityDtoMapper::toDto). // 있으면 DTO 반환
                orElse(null); // 없으면 null
    }

    // 저장
    // 클라이언트가 보낸 Todo 요청을 데이터 DB에 저장하고 저장된 결과를 응답 DTO로 반환하는 메서드
    @Transactional
    public TodoResponseDto save(TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto); // DTO -> Entity
        Todo savedTodo = todoRepository.save(todo); // DB 저장
        return EntityDtoMapper.toDto(savedTodo); // Entity -> DTO (DB에 저장된 Entity를 응답 DTO로 변환
    }

    // 수정
    // 기존 Todo를 수정
    @Transactional
    public TodoResponseDto update(Long id, TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto); // DTO -> Entity 변환
        todo.setId(id); // 아이디 설정(있으면 INSERT, 없으면 UPDATE)
        Todo updatedTodo = todoRepository.save(todo); // 저장(새 엔티티면 INSERT, 기존 id 있으면 UPDATE)
        return EntityDtoMapper.toDto(updatedTodo); // 응답용 DTO 반환
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
}
