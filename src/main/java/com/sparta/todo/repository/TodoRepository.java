package com.sparta.todo.repository;

import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByUserAndTodoId(User user, Long todoId);

//    Optional<Todo> findByTodoIdAndUserId(Long todoId, Long userId);
//
//    List<Todo> findAllByUser(User user);
//
//    List<Todo> findAllByUserIdOrderByCreatedAtDesc(Long userId); //실행시점에 시행됨


}
