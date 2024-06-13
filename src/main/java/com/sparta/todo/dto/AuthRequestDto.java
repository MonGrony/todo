package com.sparta.todo.dto;

import com.sparta.todo.entity.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthRequestDto {

    public UserRoleType UserRoleType;
    private String Username;
    private String password;

}
