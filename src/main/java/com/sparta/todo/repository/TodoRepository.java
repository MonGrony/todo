package com.sparta.todo.repository;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByUserIdAndTodoId(Long userId, Long todoId);

    List<Todo> findAllByUserId(Long userId);

    List<Todo> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    TodoResponseDto update(Long todoId, CreateTodoRequestDto requestDto);
}
