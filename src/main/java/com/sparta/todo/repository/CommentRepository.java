package com.sparta.todo.repository;

import com.sparta.todo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByTodoIdAndCommentId(Long todoId, Long commentId);

    List<Comment> findByTodoId(Long todoId);
}
