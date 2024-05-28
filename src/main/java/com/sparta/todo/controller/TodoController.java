package com.sparta.todo.controller;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

        private final TodoService todoService;

        //일정 등록
        @PostMapping("/todo")
        public TodoResponseDto createTodo(@RequestBody CreateTodoRequestDto requestDto){
            return todoService.createTodo(requestDto);
        }

        //등록된 일정 선택 조회
        @GetMapping("/todo/{todoId}")
        public TodoResponseDto getTodo(@RequestParam Long userId, @PathVariable Long todoId){
                return todoService.getTodo(userId, todoId);
        }

        //등록된 일전 전체 조회
        @GetMapping("/todo/{userId}")
        public List<TodoResponseDto> getTodoList(@PathVariable Long userId, @RequestBody TodoRequestDto requestDto ) {
                return todoService.getTodoList(userId, requestDto);
        }



}
