package com.sparta.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateTodoRequestDto {

//    UserRequestDto userRequestDto;

    private String title;
    private Long userId; //userId는 user 클래스에서 따로 받아야 함
    private String content;
    private String manager;
    private String password;

    //userid가 string 이었으니까 userIdentity
    //todo에 있던 userId 를 떼서 user 클래스에 넘겨줘야 하는 상황

//    private UserRequestDto user;

}