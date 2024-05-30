package com.sparta.todo.service;

import com.sparta.todo.dto.CommentRequestDto;
import com.sparta.todo.dto.CommentResponseDto;
import com.sparta.todo.entity.Comment;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.entity.User;
import com.sparta.todo.repository.CommentRepository;
import com.sparta.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    //댓글 등록 //예외처리: 일정이 DB에 저장되지 않은 경우
    public CommentResponseDto createComment(Long todoId, CommentRequestDto requestDto) {

        Todo todo = existCheckedTodo(todoId);
        Comment comment = commentRepository.save(new Comment(requestDto, todo));
        return new CommentResponseDto(comment);
    }

    //등록한 댓글 수정 //예외처리: 내용 이외의 것들 수정하려 시도할 때(내용만 수정 가능),
    @Transactional
    public CommentResponseDto modifyComment(Long todoId, CommentRequestDto requestDto) {

        //todoId 로 존재하는 todo인지 확인
        Todo todo = existCheckedTodo(todoId);
        Comment checkedComment = checkeSame(todo, requestDto);
        checkedComment.modify(requestDto);
        return new CommentResponseDto(checkedComment);
    }

    //선택한 일정의 댓글 삭제 //성공 메시지와 상태 코드 반환 //일정과 댓글 모두 DB에 저장되어 있어야
    //예외처리 : 일정이나 댓글의 ID 를 입력받지 않은 경우, 일정이나 댓글이 DB에 저장되어 있지 않은 경우, 선택한 댓글의 사용자가 현재 사용자와 일치하지 않은 경우

    public ResponseEntity deleteComment(Long todoId, CommentRequestDto requestDto) {

        Todo todo = existCheckedTodo(todoId);
        Comment checkedComment = checkeSame(todo, requestDto);
        commentRepository.delete(checkedComment);

        return ResponseEntity.ok().build();
    }


    private Todo existCheckedTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("해당 일정이 존재하지 않습니다."));
        return todo;
    }

    public Comment checkeSame(Todo todo, CommentRequestDto requestDto) {

        //확인된 todo의 댓글 List로 모음
        List<Comment> commentList = commentRepository.findByTodo(todo);

        //List 의 comment 들 하나씩 필터를 거쳐 수정 하려는 comment 와 requestDto 의 commentId 가 일치하는 comment 찾기
        Comment checkedComment = commentList.stream().filter(comment -> comment.getCommentId()
                        .equals(requestDto.getCommentId())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 존재하지 않습니다."));

        //comment 작성자 일치하는지 확인
        Long commentedUserId = checkedComment.getUser().getUserId();
        Long changerUserId = requestDto.getUserId();
        if (!commentedUserId.equals(changerUserId)) {
            throw new IllegalArgumentException("댓글 작성자 본인만 수정/삭제가 가능합니다.");
        }

        return checkedComment;
    }

}
