package com.sparta.todo.repository;

import com.sparta.todo.entity.Comment;
import com.sparta.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTodo(Todo todo);

//    List<Comment> findByTodoId(Long todoId);

//    Optional<Comment> findByTodoIdAndCommentId(Long todoId, Long commentId);
}
