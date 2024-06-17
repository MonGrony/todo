package com.sparta.todo.dto;

import com.sparta.todo.entity.UserRoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.sparta.todo.entity.UserRoleType.ADMIN;
import static com.sparta.todo.entity.UserRoleType.USER;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequestDto {

    private String nickname;

    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    @NotNull
    private String username;

    private boolean admin = false;

    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    @NotBlank
    private String password;

    @NotBlank
    private String adminToken = "";


}
