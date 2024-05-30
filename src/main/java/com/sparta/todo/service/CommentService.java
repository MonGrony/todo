package com.sparta.todo.service;

import com.sparta.todo.dto.CommentRequestDto;
import com.sparta.todo.dto.CommentResponseDto;
import com.sparta.todo.entity.Comment;
import com.sparta.todo.entity.Todo;
import com.sparta.todo.repository.CommentRepository;
import com.sparta.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    //댓글 등록 //예외처리: 선택한 일정의 댓글이 비어있는 경우, 일정이 DB에 저장되지 않은 경우
    public CommentResponseDto createComment(Long todoId, CommentRequestDto requestDto) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("잘못입력하셨습니다."));

        Comment comment = commentRepository.save(new Comment(requestDto));
        return new CommentResponseDto(comment);
    }

    //등록한 댓글 수정 //예외처리: comment 의 ID를 입력받지 않은 경우
    @Transactional
    public CommentResponseDto modifyComment(Long todoId, CommentRequestDto requestDto) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("해당 일정이 존재하지 않습니다."));

        List<Comment> commentList = commentRepository.findByTodo(todo);

        Comment checkedComment = commentList.stream().filter(comment -> comment.getCommentId()
                .equals(requestDto.getCommentId())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 존재하지 않습니다."));

        checkedComment.modify(requestDto);
        return new CommentResponseDto(checkedComment);
    }

    //선택한 일정의 댓글 삭제 //성공 메시지와 상태 코드 반환 //일정과 댓글 모두 DB에 저장되어 있어야 함
    //예외처리 : 일정이나 댓글의 ID 를 입력받지 않은 경우, 일정이나 댓글이 DB에 저장되어 있지 않은 경우, 선택한 댓글의 사용자가 현재 사용자와 일치하지 않은 경우











    public void check (Comment comment, CommentRequestDto requestDto){

        if (!comment.getCommentId().equals(requestDto.getCommentId())) {
            throw new NoSuchElementException("해당 댓글이 존재하지 않습니다.");
        }
//        if(!comment.getUserId().equals(requestDto.getUserId())) { //getter 가 왜 작동하지 않는지 모르겠음!!
//            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
//        }
    }

}


















