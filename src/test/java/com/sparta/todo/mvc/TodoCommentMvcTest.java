package com.sparta.todo.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todo.config.WebSecurityConfig;
import com.sparta.todo.controller.CommentController;
import com.sparta.todo.controller.TodoController;
import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.entity.UserRoleType;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.security.UserDetailsImpl;
import com.sparta.todo.service.CommentService;
import com.sparta.todo.service.TodoService;
import com.sparta.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {TodoController.class, CommentController.class}, //테스트 할 Controller 지정
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class TodoCommentMvcTest {

    private MockMvc mvc;

    private Principal mockPrincipal; //가짜 인증

    @Autowired
    private WebApplicationContext context; //

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    TodoService todoService;

    @MockBean
    CommentService commentService;

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
        for (int i = 0; i < count; i++) {
            Long todoId = (long) (i + 1);
            String title = "Title " + (i + 1);
            String content = "Content " + (i + 1);
            String manager = "Manager " + (i + 1);
            String password = "Password " + (i + 1);

            Todo todo = new Todo(todoId, title, content, manager, password);
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
        Long todoId = 3L;
        String title = "todo 제목";
        String content = "todo 내용";
        String manager = "todo 담당자";
        String password = "todo 비밀번호";
        Todo todo = new Todo(todoId, title, content, manager, password);

        this.mockUserSetup();
        Long userId= testUserDetails.getUser().getUserId();

        //when - then//등록한 유저의 일정 목록에서 선택해서 가져왔을 때
       when(todoService.getTodo(userId, todo.getTodoId()))
                .thenReturn(ResponseEntity.ok(new TodoResponseDto(todo)));

        //then//반환값들이 이러이러할 것이다
        mvc.perform(get("/api/todo/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                        .andExpect(status().isOk())
                .andDo(print());

    }
    //등록된 일전 전체 내림차순 조회 - to-do 3 개 정도 정의해야 할 듯

    //등록된 일정 선택 수정
    //등록된 일정 선택 삭제
    //댓글 등록
    //선택한 일정의 댓글 수정
    //선택한 일정의 댓글 삭제





}
