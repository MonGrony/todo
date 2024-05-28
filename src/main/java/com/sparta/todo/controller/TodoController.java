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
@RequestMapping("/api/todo")
public class TodoController {

        private final TodoService todoService;

        //일정 등록
        @PostMapping("")
        public TodoResponseDto createTodo(@RequestBody CreateTodoRequestDto requestDto) {
                return todoService.createTodo(requestDto);
        }

        //등록된 일정 선택 조회
        @GetMapping("/{todoId}")
        public TodoResponseDto getTodo(@RequestParam Long userId, @PathVariable Long todoId) {
                return todoService.getTodo(userId, todoId);
        }

        //등록된 일전 전체 조회
        @GetMapping("/{userId}")
        public List<TodoResponseDto> getTodoList(@PathVariable Long userId, @RequestBody TodoRequestDto requestDto) {
                return todoService.getTodoList(userId, requestDto);
        }

        //등록된 일정 선택 수정
        @PostMapping("/{todoId}")
        public TodoResponseDto modifyTodo(@RequestParam Long todoId, @RequestBody CreateTodoRequestDto requestDto) {
                return todoService.modifyTodo(todoId, requestDto);
        }

        //등록된 일정 선택 삭제
        @DeleteMapping("/{todoId}")
        public void deleteTodo(@PathVariable Long todoId, @RequestBody TodoRequestDto requestDto) {
                todoService.deleteTodo(todoId, requestDto);
        }


}
