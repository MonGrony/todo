package com.sparta.todo.service;

import com.sparta.todo.dto.SignupRequestDto;
import com.sparta.todo.entity.User;
import com.sparta.todo.entity.UserRoleType;
import com.sparta.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        UserRoleType role = UserRoleType.USER;
        if (requestDto.isAdmin()) { //admin 자격이 있는지 확인하기 위한 if문
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) { //계산은 여기에서
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleType.ADMIN;
        }

        User user = new User(nickname, username, role, password);
        userRepository.save(user);
    }
}
