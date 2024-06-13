package com.sparta.todo.entity;

import com.sparta.todo.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="comment")
@NoArgsConstructor
public class Comment extends Timestamped{ //BaseEntity?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name ="todo_id", nullable = false)
    private Todo todo;


    public Comment(CommentRequestDto requestDto, Todo todo) {
        this.commentId = requestDto.getCommentId();
        this.commentContent = requestDto.getCommentContent();
        this.todo = todo;
    }

    public void modify(CommentRequestDto requestDto) {
        this.commentContent = requestDto.getCommentContent();
    }

    public Long getTodoId() {
        return this.todo.getTodoId();
    }
}
