package com.sparta.todo.service;

import com.sparta.todo.dto.CommentRequestDto;
import com.sparta.todo.dto.CommentResponseDto;
import com.sparta.todo.entity.Comment;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.repository.CommentRepository;
import com.sparta.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    //댓글 등록 //예외처리: 댓글 내용이 비어있는 경우, 일정이 DB에 저장되지 않은 경우
    public CommentResponseDto createComment(Long todoId, CommentRequestDto requestDto) {

        Todo todo = existCheckedTodo(todoId);
        Comment comment = commentRepository.save(new Comment(requestDto, todo));
        return new CommentResponseDto(comment);
    }

    //등록한 댓글 수정 //예외처리: comment 의 ID를 '입력'받지 않은 경우, 댓글 내용이 비어있는 경우
    // 사용자 일치 확인 필요 //내용 이외의 것들 수정하려 시도할 때(내용만 수정 가능),
    @Transactional
    public CommentResponseDto modifyComment(Long todoId, CommentRequestDto requestDto) {

        Todo todo = existCheckedTodo(todoId);
        List<Comment> commentList = commentRepository.findByTodo(todo);

        Comment checkedComment = commentList.stream().filter(comment -> comment.getCommentId()
                .equals(requestDto.getCommentId())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 존재하지 않습니다."));

        //comment 작성자 일치 확인 필요

//        userRepository.findByCommentId

        checkedComment.modify(requestDto);
        return new CommentResponseDto(checkedComment);
    }

    private Todo existCheckedTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("해당 일정이 존재하지 않습니다."));
        return todo;
    }

    //선택한 일정의 댓글 삭제 //성공 메시지와 상태 코드 반환 //일정과 댓글 모두 DB에 저장되어 있어야
    //예외처리 : 일정이나 댓글의 ID 를 입력받지 않은 경우, 일정이나 댓글이 DB에 저장되어 있지 않은 경우, 선택한 댓글의 사용자가 현재 사용자와 일치하지 않은 경우

    public ResponseEntity deleteComment(Long todoId, CommentRequestDto requestDto) {

    }











    public void check (Comment comment, CommentRequestDto requestDto){

        if (!comment.getCommentId().equals(requestDto.getCommentId())) {
            throw new NoSuchElementException("해당 댓글이 존재하지 않습니다.");
        }
//        if(!comment.getUserId().equals(requestDto.getUserId())) { //getter 가 왜 작동하지 않는지 모르겠음!!
//            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
//        }
    }
}


















