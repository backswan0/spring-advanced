package org.example.expert.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final TodoRepository todoRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public Comment createComment(
      AuthUser authUser,
      long todoId,
      String contents
  ) {
    User userFromAuth = User.fromAuthUser(authUser);

    Todo foundTodo = todoRepository.findById(todoId)
        .orElseThrow(
            () -> new InvalidRequestException("Todo not found")
        ); // todo

    Comment commentToSave = new Comment(
        contents,
        userFromAuth,
        foundTodo
    );

    Comment savedComment = commentRepository.save(commentToSave);

    return savedComment;
  }

  @Transactional(readOnly = true)
  public List<CommentResponseDto> readAllComments(long todoId) {
    List<Comment> commentList = commentRepository
        .findAllByTodoId(todoId);

    List<CommentResponseDto> responseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      User user = comment.getUser();
      CommentResponseDto responseDto = new CommentResponseDto(
          comment.getId(),
          comment.getContents(),
          new UserResponseDto(user.getId(), user.getEmail())
      );
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }
}