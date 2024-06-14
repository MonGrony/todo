package com.sparta.todo.entity;

import com.sparta.todo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DisplayName("User Entity 테스트")
@DataJpaTest //또는 SpringBootTest
class UserTest1 {

    @Autowired
    private UserRepository userRepository;

//    @BeforeEach
//    void setUp() {
//
//    }

    //user 객체 생성이 제대로 이루어지는지 확인하는 테스트
    @Test
    @DisplayName("객체 생성 테스트 - repository 로 찾아오기")
    void createUser2() {

        //given
        User user = new User("별명", "이름", UserRoleType.USER, "비밀번호123");

        //when
        userRepository.save(user);
        User findUser = userRepository.findByUserId(user.getUserId());

        //then
        Assertions.assertEquals(user.getNickname(), findUser.getNickname());

    }


}