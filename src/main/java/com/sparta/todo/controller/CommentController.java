package com.sparta.todo.controller;

import com.sparta.todo.dto.CommentRequestDto;
import com.sparta.todo.dto.CommentResponseDto;
import com.sparta.todo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{todoId}")
public class CommentController {

    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/comment")
    public CommentResponseDto createComment(@PathVariable Long todoId, @RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(todoId, requestDto);
    }

    //선택한 일정의 댓글 수정

    @PostMapping("")
    public CommentResponseDto modifyComment(@PathVariable Long todoId, @RequestBody CommentRequestDto requestDto) {
        return commentService.modifyComment(todoId, requestDto);
    }




}
