package com.sparta.todo.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todo.config.WebSecurityConfig;
import com.sparta.todo.controller.CommentController;
import com.sparta.todo.controller.TodoController;
import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.entity.UserRoleType;
import com.sparta.todo.repository.UserRepository;
import com.sparta.todo.security.UserDetailsImpl;
import com.sparta.todo.service.CommentService;
import com.sparta.todo.service.TodoService;
import com.sparta.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = {TodoController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class TodoMvcTest {

    @Autowired
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    TodoService todoService;

    @MockBean
    CommentService commentService;

    @MockBean
    UserRepository userRepository;

    private UserDetailsImpl testUserDetails;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    public void mockUserSetup() {
        String nickname = "salad55";
        String username = "sarah3";
        UserRoleType role = UserRoleType.USER;
        String password = "qlalfqjsgh123";

        User testUser = new User(nickname, username, role, password);
        testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, testUserDetails.getAuthorities());
    }

    public List<Todo> makeMockTodoList(int count) {
        List<Todo> mockTodoList = new ArrayList<>();
        User user = new User(1L, "nick이름", "name", UserRoleType.USER, "qlalfqjsgh12345");
        for (int i = 0; i < count; i++) {
            Long todoId = (long) (i + 1);
            String title = "Title " + (i + 1);
            String content = "Content " + (i + 1);
            String manager = "Manager " + (i + 1);
            String password = "Password " + (i + 1);
//            LocalDateTime createdAt = LocalDateTime.now();
            Todo todo = new Todo(todoId, title, content, manager, password, user);
            mockTodoList.add(todo);
        }
        return mockTodoList;
    }

    @Test
    @DisplayName("Todo 등록 요청 처리")
    void test1() throws Exception {
        // given
        this.mockUserSetup();
        CreateTodoRequestDto requestDto = CreateTodoRequestDto.builder()
                .title("할 일 정함11")
                .content("상세내용 입니다")
                .manager("관리자")
                .password("비밀번호123")
                .build();

        String postInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/api/todo")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andDo(print());
    }

    @Test
    @DisplayName("등록된 일정 선택 조회 - Status.Ok")
    public void test2() throws Exception {
        //given
//        Long todoId = 3L;
//        String title = "todo 제목";
//        String content = "todo 내용";
//        String manager = "todo 담당자";
//        String password = "todo 비밀번호";
//        Todo todo = new Todo(todoId, title, content, manager, password);

        List<Todo> mockTodoList = makeMockTodoList(2);
        this.mockUserSetup();
        Long userId = mockTodoList.get(1).getUser().getUserId();

        //when - then//등록한 유저의 일정 목록에서 선택해서 가져왔을 때
        when(todoService.getTodo(userId, 2L))
                .thenReturn(ResponseEntity.ok(new TodoResponseDto(mockTodoList.get(1))));

        //then//반환값들이 이러이러할 것이다
        mvc.perform(get("/api/todo/{todoId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());

    }

    //등록된 일전 전체 내림차순 조회 - to-do 3 개 정도 정의해야 할 듯
    @Test
    @DisplayName("등록된 일전 전체 내림차순 조회 - 성공")
    public void test3() throws Exception {
        //given
        this.mockUserSetup();

        List<Todo> mockTodoList = makeMockTodoList(3);
        TodoResponseDto response1 = new TodoResponseDto(mockTodoList.get(0));
        TodoResponseDto response2 = new TodoResponseDto(mockTodoList.get(1));
        TodoResponseDto response3 = new TodoResponseDto(mockTodoList.get(2));
        List<TodoResponseDto> todoResponseDtoList = Arrays.asList(response1, response2, response3);

        //when //userId 로 조회했을 때 //TodoService.getTodoList 를 사용했을 때
        when(todoService.getTodoList(1L)).thenReturn(todoResponseDtoList);

        //then //생성시간 내림차순으로 조회됨
        mvc.perform(get("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

//                .andExpect(jsonPath("$[0].todoId", is(3L)))
//                .andExpect(jsonPath("$[1].todoId", is(2L)))
//                .andExpect(jsonPath("$[2].todoId", is(1L)))
//                .andExpect(jsonPath("$").value(hasSize(3)))
//                .andExpect(jsonPath("$[0].todoId").value(3L))
//                .andExpect(jsonPath("$[1].todoId").value(2L))
//                .andExpect(jsonPath("$[2].todoId").value(1L))

    @Test
    @DisplayName("등록된 일정 선택 수정")
    public void test4() throws Exception {

        //given
        // mock 인증유저 세팅, 등록된 to-do 목록 세팅
        this.mockUserSetup();
        List<Todo> mockTodoList = makeMockTodoList(5);
        //수정할 to-do 선택과 수정할 내용
        CreateTodoRequestDto requestDto = CreateTodoRequestDto.builder()
                .title("할 일 정함3-1")
                .content("상세내용3-1 입니다")
                .manager("관리자3-1")
                .password("비밀번호321-1")
                .build();

        String modifyInfo = objectMapper.writeValueAsString(requestDto);

        Todo mockTodo = mockTodoList.get(2);
        Long todoId = mockTodo.getTodoId();
        Todo modifiedMockTodo = new Todo(todoId, requestDto);
        TodoResponseDto mockResponseDto = new TodoResponseDto(modifiedMockTodo);

        //when
        when(todoService.modifyTodo(todoId, testUserDetails, requestDto)).thenReturn(mockResponseDto);
        //then
        ResultActions resultActions = mvc.perform(post("/api/todo/{todoId}", todoId)
                        .content(modifyInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("JSON Response: " + jsonResponse);

//        resultActions
//                .andExpect(jsonPath("$.todo.@todoId", is(todoId)))
//                .andDo(print());

//                .andExpect(jsonPath("$.manager", is("관리자3-1")))
//                .andExpect(jsonPath("$.content", is("상세내용3-1 입니다")))
//                .andExpect(status().isOk())
    }

    @Test
    @DisplayName("등록된 일정 선택 삭제 - 성공")
    public void test() throws Exception {
        //given
        Long todoId = 1L;
        this.mockUserSetup();
        Long userId = testUserDetails.getUser().getUserId();
        String password = "qlalfqjsgh123";
        TodoRequestDto requestDto = new TodoRequestDto(userId, password);

        //when
        when(todoService.deleteTodo(todoId, testUserDetails, requestDto))
                .thenThrow(new RuntimeException("TodoService.deleteTodo 호출 예외"));

        String deleteInfo = objectMapper.writeValueAsString(requestDto);

        //then
        ResultActions resultActions =
                mvc.perform(delete("/api/todo/{todoId}", todoId)
                                .content(deleteInfo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                        .andExpect(status().isNoContent())
                        .andDo(print());
    }

}
