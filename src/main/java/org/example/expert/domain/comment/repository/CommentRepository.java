package org.example.expert.domain.comment.repository;

import java.util.List;
import org.example.expert.common.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @EntityGraph(attributePaths = {"user", "todo"})
  List<Comment> findAllByTodoId(Long todoId);
}