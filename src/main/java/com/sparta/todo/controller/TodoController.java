package com.sparta.todo.controller;

import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

        private final TodoService todoService;

        //일정 등록
        @PostMapping("/todo")
        public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto){
            return todoService.createTodo(requestDto);
        }

        //등록된 일정 선택 조회
        @GetMapping("/todo/{todoId}")
        public TodoResponseDto callTodo(@RequestParam Long userId, @PathVariable Long todoId){
                return todoService.callTodo(userId, todoId);
        }



}
