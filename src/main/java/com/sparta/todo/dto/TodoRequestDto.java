package com.sparta.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoRequestDto { //사용자 입장

    private String userId;
    private String password;

}
