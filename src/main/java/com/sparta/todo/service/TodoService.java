package com.sparta.todo.service;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.repository.UserRepository;
import com.sparta.todo.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //일정 등록
    public TodoResponseDto createTodo(CreateTodoRequestDto requestDto, User user) {
        Long userId = user.getUserId();
        User finduser = findUser(userId);
        Todo todo = new Todo(requestDto, finduser);
        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponseDto(savedTodo);
    }

    //등록된 일정 선택 조회
    public ResponseEntity<TodoResponseDto> getTodo(Long userId, Long todoId) {
        User user = findUser(userId);
        Todo todo = todoRepository.findByUserAndTodoId(user, todoId)
                .orElseThrow(() -> new EntityNotFoundException("등록된 일정이 없습니다."));
        TodoResponseDto responseDto = new TodoResponseDto(todo);
        return ResponseEntity.ok(responseDto);
    }

    //등록된 일정 전체 조회
    public List<TodoResponseDto> getTodoList(Long userId) {

        User user = userRepository.findByUserId(userId);
        List<TodoResponseDto> todolist = todoRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream().map(TodoResponseDto::new).toList();

        if (todolist.isEmpty()) {
            throw new RuntimeException("등록된 일정이 없습니다.");
        }
        return todolist;
    }


    //등록된 일정 선택 수정
    @Transactional
    public TodoResponseDto modifyTodo(Long todoId, UserDetailsImpl userDetails, CreateTodoRequestDto requestDto) {
        Long userId = userDetails.getUser().getUserId();
        String ps = userDetails.getUser().getPassword();
        String forCheckPW = requestDto.getPassword();
        check(userId,ps, forCheckPW);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("해당 일정을 찾을 수 없습니다")
                );

        todo.modify(requestDto);

        return new TodoResponseDto(todo);
    }

    //user 본인이 등록했던 일정 선택 삭제

    @Transactional
    public ResponseEntity deleteTodo(Long todoId, UserDetailsImpl userDetails, TodoRequestDto requestDto) {
        Long userId = userDetails.getUser().getUserId();
        String ps = userDetails.getUser().getPassword();
        Long forCheckUserId = requestDto.getUserId();
        String forCheckPW = requestDto.getPassword();
        check(userId, forCheckUserId, ps, forCheckPW);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("해당 일정을 찾을 수 없습니다")
                );

        //삭제
        todoRepository.deleteById(todoId);
        return ResponseEntity.noContent().build();
    }

    //userId 에 등록된 일정이 있는지 확인하는 메서드

    private void check(Long userId, String password, String PW) {
        if (!userId.equals(userId)) {
            throw new RuntimeException("본인의 것만 수정 가능합니다");
        }

        // >> 비밀번호 일치 확인
        if (!passwordEncoder.matches( password, PW)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    //등록된 userId 인지, 그 userId 에 맞는 password 인지 확인하는 메서드
    private void check(Long userId, Long forCheckUserId, String password, String PW) {

        if (!userId.equals(forCheckUserId)) {
            throw new RuntimeException("본인의 것만 수정 가능합니다");
        }

        // >> 비밀번호 일치 확인
        if (!passwordEncoder.matches( password, PW)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private User findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));
        return user;
    }

}



