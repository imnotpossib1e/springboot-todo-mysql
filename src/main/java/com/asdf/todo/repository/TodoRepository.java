package com.asdf.todo.repository;

import com.asdf.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// Todo 테이블에 대한 CRUD를 자동으로 처리하는 Repository
// JpaRepository<엔티티 타입, PK 타입>
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>  { }