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
    private String nickname;//빈칸 허용!
    @NotBlank
    private String username;
    private UserRoleType roleType;
    @NotBlank
    private String password;
    private String authority;

}
