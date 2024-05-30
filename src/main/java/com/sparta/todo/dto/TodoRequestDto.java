package com.sparta.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoRequestDto { //사용자 입장

    @NotBlank
    private Long userId;
    @NotBlank
    private String password;

}
