package com.sparta.todo.service;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TodoService { //HTTP 상태코드 전송 설정 필요

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    //일정 등록
    public TodoResponseDto createTodo(CreateTodoRequestDto requestDto) {
        Todo todo = todoRepository.save(new Todo(requestDto));
        return new TodoResponseDto(todo);
    }

    //등록된 일정 선택 조회
    public TodoResponseDto getTodo(Long userId, Long todoId) {
        Todo todo = todoRepository.findByUserIdAndTodoId(userId, todoId)
                .orElseThrow(() -> new RuntimeException("해당 일정을 찾을 수 없습니다.")
                );
        return new TodoResponseDto(todo);
    }

    //등록된 일정 전체 조회 //내림차순 정렬 추가 필요
    public List<TodoResponseDto> getTodoList(Long userId, TodoRequestDto requestDto) {
        String ps = requestDto.getPassword();
        List<TodoResponseDto> allTodoList = new ArrayList<>();

        List<Todo> todoList = check(userId, ps);

        for (Todo todo : todoList) {
            TodoResponseDto responseDto = new TodoResponseDto(todo);
            allTodoList.add(responseDto);
        }
        return allTodoList;
    }

    //등록된 일정 선택 수정 //확인할 것: ResponseDto 로 돌려줄 때, 시간이 바뀌는건 아닌지
    public TodoResponseDto modifyTodo(Long todoId, CreateTodoRequestDto requestDto) {
        Long userId = requestDto.getUserId();
        String ps = requestDto.getPassword();
        TodoResponseDto responseDto = new TodoResponseDto();

        List<Todo> todoList = check(userId, ps);

        //password 일치하므로 그 user 의 일정 중에서 todoId 로 찾은 게시글 수정

        for (Todo todo : todoList) {
            if (todo.getTodoId().equals(todoId)) {
                Todo modifiedTodo = new Todo(requestDto);

                responseDto = new TodoResponseDto(modifiedTodo);
            }
        }
        return responseDto;
    }

    //user 본인인 등록했던 일정 선택 삭제
    public ResponseEntity deleteTodo(Long todoId, TodoRequestDto requestDto) {
        Long userId = requestDto.getUserId();
        String ps = requestDto.getPassword();

        List<Todo> todoList = check(userId, ps);

        for(Todo todo : todoList ){
            if (!todo.getTodoId().equals(todoId)) {
                throw new NoSuchElementException("등록되지 않은 일정입니다.");
            }
            //삭제
            todoRepository.deleteById(todoId);
        }
        return ResponseEntity.noContent().build();
    }

    //등록된 userId 인지, 그 userId 에 맞는 password 인지, userId 에 등록된 일정이 있는지, 확인하는 메서드 (예정)
    private List<Todo> check(Long userId, String password) {

         User user = userRepository.findById(userId).orElseThrow(()->
            new RuntimeException("해당 사용자를 찾을 수 없습니다.")
        );

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<Todo> todoList = todoRepository.findAllByUserId(userId);
        if (todoList.isEmpty()) {
            throw new RuntimeException("등록된 일정이 없습니다.");
        }
        return todoList;
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}

