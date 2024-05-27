package com.sparta.todo.dto;


import com.sparta.todo.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class TodoResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;

    private LocalDateTime createdAt;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.writer = todo.getWriter();

        this.createdAt = todo.getCreatedAt();

    }
}
