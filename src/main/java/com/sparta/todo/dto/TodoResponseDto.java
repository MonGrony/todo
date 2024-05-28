package com.sparta.todo.dto;


import com.sparta.todo.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class TodoResponseDto {

    private Long todoId;
    private Long userId;
    private String title;
    private String content;
    private String manager;

    private LocalDateTime createdAt;

    public TodoResponseDto(Todo todo) {
        this.todoId = todo.getTodoId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.manager = todo.getManager();

        this.createdAt = todo.getCreatedAt();

    }
}
