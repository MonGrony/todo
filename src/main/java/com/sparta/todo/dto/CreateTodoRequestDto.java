package com.sparta.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CreateTodoRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank
    private Long userId; //userId는 user 클래스에서 따로 받아야 함
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private String manager;
    @NotBlank(message = "password 는 필수입니다.")
    private String password;

    //userid가 string 이었으니까 userIdentity
    //todo에 있던 userId 를 떼서 user 클래스에 넘겨줘야 하는 상황


}