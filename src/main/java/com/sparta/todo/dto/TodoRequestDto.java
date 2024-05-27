package com.sparta.todo.dto;

import com.sparta.todo.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoRequestDto {

    private String title;
    private String content;
    private String writer;
    private String password;

}
