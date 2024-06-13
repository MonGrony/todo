package com.sparta.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank
    private Long commentId;
    @NotBlank(message = "내용을 입력해주세요.")
    private String commentContent;
    private Long userId;

}
