package com.sparta.todo.entity;

import com.sparta.todo.dto.CreateTodoRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Table(name = "todo")
@Entity
@NoArgsConstructor
public class Todo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

//    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)//
//    private Long userId; //user_id로 인식

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String manager;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //user 를 참조하지만 key 의 주인은 user 다 (Table column 에 user_id 열이 생김)

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    public Todo(CreateTodoRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.manager = requestDto.getManager();
        this.password = requestDto.getPassword();
    }

    public Todo(Long todoId, String title, String content, String manager, String password, User user) {
        this.todoId=todoId;
        this.title = title;
        this.content = content;
        this.manager = manager;
        this.password = password;
        this.user = user;
    }

    public void modify(CreateTodoRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.manager = requestDto.getManager();
    }

    public Todo(CreateTodoRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.user = user;
        this.content = requestDto.getContent();
        this.manager = requestDto.getManager();
        this.password = requestDto.getPassword();
    }

    public Todo(Long todoId, String title, String content, String manager, String password) {
        this.todoId = todoId;
        this.title = title;
        this.content = content;
        this.manager = manager;
        this.password = password;
    }
    public Todo(Long todoId, CreateTodoRequestDto requestDto) {
        this.todoId = todoId;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.manager = requestDto.getManager();
        this.password = requestDto.getPassword();
    }



}
