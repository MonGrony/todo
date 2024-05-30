package com.sparta.todo.dto;

import jakarta.persistence.Column;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {

    @NotEmpty
    private Long commentId;
    @NotEmpty(message = "등록할 내용이 없습니다.")
    private String commentContent;
    private Long userId;

}
