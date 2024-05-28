package com.sparta.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateTodoRequestDto {

    private String title;
    private Long userId;
    private String content;
    private String manager;
    private String password;

}