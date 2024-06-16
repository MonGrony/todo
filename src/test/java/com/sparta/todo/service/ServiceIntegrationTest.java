package com.sparta.todo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.entity.UserRoleType;
import com.sparta.todo.mvc.MockSpringSecurityFilter;
import com.sparta.todo.repository.UserRepository;
import com.sparta.todo.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles
@Disabled
public class ServiceIntegrationTest {

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

        User testUser = new User(1L, nickname, username, role, password);
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



    // 시나리오

    // 회원가입
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
