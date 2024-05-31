package com.sparta.todo.dto;


import com.sparta.todo.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String commentContent;
    private LocalDateTime createdAt;


    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.commentContent = comment.getCommentContent();
        this.createdAt = comment.getCreatedAt();
    }
}
