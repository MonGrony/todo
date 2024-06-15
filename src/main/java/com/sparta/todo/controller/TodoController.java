package com.sparta.todo.controller;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.User;
import com.sparta.todo.security.UserDetailsImpl;
import com.sparta.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    //일정 등록
    @PostMapping("")
    public TodoResponseDto createTodo(@RequestBody @Valid CreateTodoRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return todoService.createTodo(requestDto, user);
    }

    //등록된 일정 선택 조회
    @GetMapping("/{todoId}")
    public TodoResponseDto getTodo(@PathVariable Long todoId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId();
        return todoService.getTodo(userId, todoId);
    }

    //등록된 일전 전체 조회
    @GetMapping("") //todoId 만 가능
    public List<TodoResponseDto> getTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId();
        return todoService.getTodoList(userId);
    }

    //등록된 일정 선택 수정
    @PostMapping("/{todoId}")
    public TodoResponseDto modifyTodo(@RequestParam Long todoId, @RequestBody @Valid CreateTodoRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.modifyTodo(todoId, userDetails, requestDto);
    }

    //등록된 일정 선택 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId, @RequestBody TodoRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        todoService.deleteTodo(todoId, userDetails);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
