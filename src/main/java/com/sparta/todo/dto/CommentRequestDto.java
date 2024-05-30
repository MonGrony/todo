package com.sparta.todo.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {

    private Long commentId;
    private String commentContent;
    private Long UserId;

}
