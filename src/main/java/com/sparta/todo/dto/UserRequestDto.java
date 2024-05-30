package com.sparta.todo.dto;

import com.sparta.todo.entity.UserRoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    private Long userId;
    private String nickName;
    @NotBlank
    private String userName;
    private UserRoleType roleType;
    @NotBlank
    private String password;
    private String authority;

}
