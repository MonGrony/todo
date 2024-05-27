package com.sparta.todo.service;

import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    //일정 등록 메서드
    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        Todo todo = todoRepository.save(new Todo(requestDto));
        return new TodoResponseDto(todo);
    }
}
