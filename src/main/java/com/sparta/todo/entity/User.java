package com.sparta.todo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleType roleType;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todoList = new ArrayList<>();

    public User(String nickname, String username, UserRoleType roleType, String password) {
        this.nickname = nickname;
        this.username = username;
        this.roleType = roleType;
        this.password = password;
    }

    public User(Long userId, String nickname, String username, UserRoleType roleType, String password) {
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
        this.roleType = roleType;
        this.password = password;
    }
}
