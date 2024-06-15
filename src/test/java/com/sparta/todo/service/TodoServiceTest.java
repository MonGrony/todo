package com.sparta.todo.service;

import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    TodoRepository todoRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    private TodoService todoService;

    @BeforeEach
    void setUp(){
        //작동시키려면 Service 클래스가 필요하므로 생성자 Service 초기화
        todoService = new TodoService(todoRepository, userRepository, passwordEncoder);
    }

    // createTodo 메서드 작동 후
    // 등록한 내용과 repository 에 등록된 것과 동일한 내용인지 확인
    @Test
    @DisplayName("Todo 등록 후 반영")
    public void enrollTodo(){
        //given
        CreateTodoRequestDto requestDto = CreateTodoRequestDto.builder()
        .title("제목입니다")
        .content("내용입니다")
        .manager("관리자입니다")
        .password("비밀번호123%")
        .build();

        //repository 를 통해 requestDto 내용을 DB에 저장해야만 responseDto 로 꺼낼 수 있으므로 repository.save 를 사용해야 함
        // save 에는 Entity 가 필요하므로 user 찾아야 함 -> user 설정해줌
        // save 후 to-do 를 반환하므로 to-do Entity 설정해 줌
        User user = Mockito.mock(User.class);
        Todo todo = Mockito.mock(Todo.class);

        //when
        when(userRepository.findById(anyLong())).thenReturn( //anyLong 대신 user.getUserId() 도 가능
                Optional.of(user));//findById 가 user 를 optional 로 반환 하기 때문에 optional 로 감싸서 반환
        when(todoRepository.save(ArgumentMatchers.any(Todo.class))).thenReturn(
                todo);

        //responseDto 를 정의해야 하는데, responseDto 는 DB에 만들어진 객체에서 정보를 빼서 씀 - 그 객체가 앞에 만든 to-do 임
        //to-do mock 이므로 내부는 비어있기에 to-do 에 내용물을 넣어주는 것
        given(todo.getTodoId()).willReturn(1L); //todoId 는 원래 자동생성인데 임의로 부여
        given(todo.getTitle()).willReturn(requestDto.getTitle());
        given(todo.getContent()).willReturn(requestDto.getContent());
        given(todo.getManager()).willReturn(requestDto.getManager());

        TodoResponseDto responseDto = todoService.createTodo(requestDto, user); //to-doService 의 createTodo 메서드 작동 후 반환값이 TodoResponseDto 임

        //then - responseDto 에 잘 반영되는지 확인
        assertEquals(requestDto.getTitle(),responseDto.getTitle());
        assertEquals(requestDto.getContent(),responseDto.getContent());
        assertEquals(requestDto.getManager(),responseDto.getManager());

    }

    //일정 선택 조회 시 한 user 가진 to-do 에 없는 to-doId를 입력한 경우 exception 발생
    @Test
    @DisplayName("getTodo 예외 처리")
    public void getTodoException(){

        //given
        Long userId = 123L;
        User user = Mockito.mock(User.class);

        //입력한 userId 값이 무엇인지와 상관 없이 user 를 return 한다 //다음 단계로 가야 하니까 무조건 통과해야함
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        // when 등록되지 않은 todoId(3L)로 repository 에서 to-do 찾기 (getTodo)
        when(todoRepository.findByUserAndTodoId(ArgumentMatchers.any(), anyLong()))
                .thenThrow(new EntityNotFoundException("등록된 일정이 없습니다."));

        //then //Exception 발생할 것이다
        assertThrows(EntityNotFoundException.class, () -> {
            todoService.getTodo(userId, 3L);
        });
    }

    //등록된 일정 전체 조회시 여러 개가 Desc 로 나오는지, 없을 때 exception 발생 여부


}