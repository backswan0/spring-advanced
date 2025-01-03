package org.example.expert.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.EntityFinderUtil;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
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

    Todo foundTodo = EntityFinderUtil.findEntityById(
        todoRepository,
        todoId,
        Todo.class
    );

    Comment commentToSave = new Comment(
        contents,
        userFromAuth,
        foundTodo
    );

    Comment savedComment = commentRepository.save(commentToSave);

    return savedComment;
  }

  @Transactional(readOnly = true)
  public List<Comment> readAllComments(long todoId) {
    List<Comment> commentList = new ArrayList<>();

    commentList = commentRepository.findAllByTodoId(todoId);

    return commentList;
  }
}