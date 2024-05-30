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
    private Long UserId;

    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.commentContent = comment.getCommentContent();
//        this.UserId = comment.getUserId();

        this.createdAt = comment.getCreatedAt();
    }
}
