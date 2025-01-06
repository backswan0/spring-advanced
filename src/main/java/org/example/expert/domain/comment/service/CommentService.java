package org.example.expert.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.entity.Comment;
import org.example.expert.common.entity.Todo;
import org.example.expert.common.entity.User;
import org.example.expert.common.util.EntityFinderUtil;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.comment.dto.request.CreateCommentRequestDto;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.dto.response.CreateCommentResponseDto;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final TodoRepository todoRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public CreateCommentResponseDto createComment(
      AuthUserDto authUserDto,
      long todoId,
      CreateCommentRequestDto requestDto
  ) {

    // 인증된 사용자 정보에서 User 객체를 생성
    User userFromAuth = User.fromAuthUser(authUserDto);

    // todoId를 이용해 해당 엔티티 조회 (예외 처리 포함)
    Todo foundTodo = EntityFinderUtil.findEntityById(
        todoRepository,
        todoId,
        Todo.class
    );

    Comment commentToSave = new Comment(
        requestDto.contents(),
        userFromAuth,
        foundTodo
    );

    Comment savedComment = commentRepository.save(commentToSave);

    return new CreateCommentResponseDto(
        savedComment.getId(),
        savedComment.getContents(),
        new UserResponseDto(
            userFromAuth.getId(),
            userFromAuth.getEmail()
        )
    );
  }

  @Transactional(readOnly = true)
  public List<CommentResponseDto> readAllComments(long todoId) {
    List<Comment> commentList = new ArrayList<>();

    commentList = commentRepository.findAllByTodoId(todoId);

    List<CommentResponseDto> commentDtoList = new ArrayList<>();

    commentDtoList = commentList.stream()
        .map(comment -> {
              // 댓글 작성자의 정보를 UserResponseDto로 변환
              UserResponseDto responseDto = new UserResponseDto(
                  comment.getUser().getId(),
                  comment.getUser().getEmail()
              );
              return new CommentResponseDto(
                  comment.getId(),
                  comment.getContents(),
                  responseDto
              );
            }
        ).toList();

    return commentDtoList;
  }
}