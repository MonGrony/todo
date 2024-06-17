package com.sparta.todo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todo.controller.UserController;
import com.sparta.todo.dto.SignupRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.entity.UserRoleType;
import com.sparta.todo.mvc.MockSpringSecurityFilter;
import com.sparta.todo.repository.CommentRepository;
import com.sparta.todo.repository.TodoRepository;
import com.sparta.todo.repository.UserRepository;
import com.sparta.todo.security.UserDetailsImpl;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@WebMvcTest(
//        controllers = {UserController.class},
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = SecurityConfig.class)})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles
public class ServiceIntegrationTest {

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    ResultActions resultActions;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    public void mockUserSetup() {
        String nickname = "tester55";
        String username = "integrationTester";
        UserRoleType role = UserRoleType.USER;
        String password = "testPassword123";

        User testUser = new User(1L, nickname, username, role, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
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

    // 회원가입
    @Test
    @DisplayName("회원가입 성공")
    public void signup_success() throws Exception {
        //given
        //첫번째 유저
        MultiValueMap<String, String> signupRequestForm1 = new LinkedMultiValueMap<>();
        signupRequestForm1.add("nickname", "첫번째111");
        signupRequestForm1.add("username", "member1");
        signupRequestForm1.add("password", "password111");
        signupRequestForm1.add("admin", "false");
        signupRequestForm1.add("adminToken", "abc1231");
        //두번째 유저
        MultiValueMap<String, String> signupRequestForm2 = new LinkedMultiValueMap<>();
        signupRequestForm2.add("nickname", "두번째222");
        signupRequestForm2.add("username", "member2");
        signupRequestForm2.add("password", "password222");
        signupRequestForm2.add("admin", "false");
        signupRequestForm2.add("adminToken", "abc1232");
        //세번째 유저
        MultiValueMap<String, String> signupRequestForm3 = new LinkedMultiValueMap<>();
        signupRequestForm3.add("nickname", "세번째333");
        signupRequestForm3.add("username", "member3");
        signupRequestForm3.add("password", "password333");
        signupRequestForm3.add("admin", "false");
        signupRequestForm3.add("adminToken", "abc1233");

        // when - then
        mvc.perform(post("/api/user/signup")
                        .params(signupRequestForm1)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/user/login-page"))
                .andDo(print());
        mvc.perform(post("/api/user/signup")
                        .params(signupRequestForm2)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/user/login-page"))
                .andDo(print());
      mvc.perform(post("/api/user/signup")
                        .params(signupRequestForm3)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/user/login-page"))
                .andDo(print());


//        mvc.perform(post("/api/user/signup")
//                        .content(objectMapper.writeValueAsString(requestDto3))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/api/user/login-page"))
//                .andDo(print());
    }

    // 로그인
    //

    // to-do 5개 작성 / 타인이 to-do 2개 작성
    // comment 1번 to-do에 2개 작성 / 타인의 to-do 에 각각 1개씩 작성 (총 2개)
    // 타인1 이 내 1번 to-do에 comment 작성

    // 로그아웃 후 재로그인

    // 본인의 to-do 전체 보기
    // to-do 3번 삭제 / 타인의 to-do 삭제 시도
    // to-do 2번 수정(manager 변경) / 타인의 to-do 수정 시도
    // 삭제한 to-do 조회 시도

    // to-do 1번의 comment 1번 수정 / 타인의 comment 수정 시도
    // to-do 1번의 comment 2번 삭제 / 타인의 comment 삭제 시도
    // 삭제한 comment 조회 시도

    // 회원 탈퇴
}

