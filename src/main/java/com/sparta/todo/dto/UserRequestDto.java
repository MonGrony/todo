package com.sparta.todo.dto;

import com.sparta.todo.entity.UserRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    private Long userId;
    private String nickName;
    private String userName; //login 할때
    private UserRoleType roleType;
    private String password;
    private String authority;

}
