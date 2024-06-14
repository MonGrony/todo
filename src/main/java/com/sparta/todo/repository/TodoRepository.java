package com.sparta.todo.repository;

import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByUserAndTodoId(User user, Long todoId);

    List<Todo> findAllByUserOrderByCreatedAtDesc(User user); //실행시점에 시행됨

//    Optional<Todo> findByTodoIdAndUserId(Long todoId, Long userId);
//
//    List<Todo> findAllByUser(User user);
//


}
