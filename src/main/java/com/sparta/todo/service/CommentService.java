package com.sparta.todo.service;

import com.sparta.todo.dto.CommentRequestDto;
import com.sparta.todo.dto.CommentResponseDto;
import com.sparta.todo.entity.Comment;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.repository.CommentRepository;
import com.sparta.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    //댓글 등록
    public CommentResponseDto createComment(Long todoId, CommentRequestDto requestDto) {
        //todoId 로 일정 있는지 확인 (댓글 다는 거니까 원글 주인 아니어도 됨)
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("잘못입력하셨습니다."));

        Comment comment = commentRepository.save(new Comment(requestDto));
        return new CommentResponseDto(comment);
    }


}
