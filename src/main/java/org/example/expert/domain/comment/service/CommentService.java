package org.example.expert.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.request.CreateCommentRequestDto;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.dto.response.CreateCommentResponseDto;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

  private final TodoRepository todoRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public CreateCommentResponseDto createComment(
      AuthUser authUser,
      long todoId,
      CreateCommentRequestDto requestDto
  ) {
    User user = User.fromAuthUser(authUser);
    Todo todo = todoRepository
        .findById(todoId)
        .orElseThrow(
            () -> new InvalidRequestException("Todo not found")
        );

    Comment comment = new Comment(
        requestDto.getContents(),
        user,
        todo
    );

    Comment savedComment = commentRepository.save(comment);

    return new CreateCommentResponseDto(
        savedComment.getId(),
        savedComment.getContents(),
        new UserResponse(user.getId(), user.getEmail())
    );
  }

  public List<CommentResponseDto> readAllComments(long todoId) {
    List<Comment> commentList = commentRepository
        .findAllByTodoId(todoId);

    List<CommentResponseDto> responseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      User user = comment.getUser();
      CommentResponseDto responseDto = new CommentResponseDto(
          comment.getId(),
          comment.getContents(),
          new UserResponse(user.getId(), user.getEmail())
      );
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }
}