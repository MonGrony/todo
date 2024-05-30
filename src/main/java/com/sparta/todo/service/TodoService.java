package com.sparta.todo.service;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    //일정 등록
    public TodoResponseDto createTodo(CreateTodoRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(""));
        Todo todo = new Todo(requestDto, user);
        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponseDto(savedTodo);
    }

    //등록된 일정 선택 조회
    public TodoResponseDto getTodo(Long userId, Long todoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(""));

        Todo todo = todoRepository.findByUserAndTodoId(user, todoId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return new TodoResponseDto(todo);
    }

    //등록된 일정 전체 조회
    public List<TodoResponseDto> getTodoList(Long userId, TodoRequestDto requestDto) {
        User user1 = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
        return todoRepository.findAll().stream()
                .map(TodoResponseDto::new).toList();
    }

    //등록된 일정 선택 수정
    @Transactional
    public TodoResponseDto modifyTodo(Long todoId, CreateTodoRequestDto requestDto) {
//        String username = requestDto.getUser().getUserName();
        Long userId = requestDto.getUserId();
        String ps = requestDto.getPassword();//
        check(userId, ps);

//        if (!todo.getUserId().equals(userId)) {
//            throw new IllegalArgumentException("해당 일정에 대한 수정 권한이 없습니다.");
//        }

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("해당 일정을 찾을 수 없습니다")
                );

        //수정
        todo.modify(requestDto);

        return new TodoResponseDto(todo);
    }

    //user 본인이 등록했던 일정 선택 삭제
    public ResponseEntity deleteTodo(Long todoId, TodoRequestDto requestDto) {
        Long userId = requestDto.getUserId();
        String ps = requestDto.getPassword();
        check(userId, ps);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("해당 일정을 찾을 수 없습니다")
                );
        //삭제
        todoRepository.deleteById(todoId);
        return ResponseEntity.noContent().build();
    }

    private List<Todo> getTodoList(Long userId) {
        User user1 = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
        List<Todo> todoList = todoRepository.findAll();
        if (todoList.isEmpty()) {
            throw new RuntimeException("등록된 일정이 없습니다.");
        }
        return todoList;
    }

    //등록된 userId 인지, 그 userId 에 맞는 password 인지, userId 에 등록된 일정이 있는지, 확인하는 메서드
    private void check(Long userId, String password) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("해당 사용자를 찾을 수 없습니다.")
        );

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}


