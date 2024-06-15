package com.sparta.todo.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todo.config.WebSecurityConfig;
import com.sparta.todo.controller.CommentController;
import com.sparta.todo.controller.TodoController;
import com.sparta.todo.dto.CreateTodoRequestDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.entity.UserRoleType;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, testUserDetails.getAuthorities());
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

    //등록된 일정 선택 조회
    //등록된 일전 전체 조회
    //등록된 일정 선택 수정
    //등록된 일정 선택 삭제
    //댓글 등록
    //선택한 일정의 댓글 수정
    //선택한 일정의 댓글 삭제





}
