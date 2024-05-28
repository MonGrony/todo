package com.sparta.todo.service;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TodoService {

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
                .orElseThrow(()-> new RuntimeException("해당 일정을 찾을 수 없습니다.")
                );
        return new TodoResponseDto(todo);
    }

    //등록된 일정 전체 조회
    public List<TodoResponseDto> getTodoList(Long userId, TodoRequestDto requestDto) {
        String ps = requestDto.getPassword();
        List<TodoResponseDto> TodoList = new ArrayList<>();

        //userRepository 에서 userId 로 존재확인 후, 그 userId 의 password 와 일치하는지 확인
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            //password 일치가 확인되면 사용자가 입력했던 todo를 모두 보여준다
            if(!user.get().getPassword().equals(ps)) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            } else {
                for (Todo todo : todoRepository.findAllByUserId(userId)) {
                    TodoResponseDto todoResponseDto = new TodoResponseDto(todo);
                    TodoList.add(todoResponseDto);
                }
            }
        } else {
            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
        }
        return TodoList;
    }
}
