package com.sparta.todo.entity;

import com.sparta.todo.repository.UserRepository;
import com.sparta.todo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Entity 테스트2")
public class UserTest2 {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    //user 객체 생성이 제대로 이루어지는지 확인하는 테스트
    @Test
    @DisplayName("객체 생성 테스트 - 단순 확인")
    void createUser1() {
        User user = new User("별명", "이름", UserRoleType.USER, "비밀번호123");

        assertThat(user.getNickname()).isEqualTo("별명");

    }


}
