package org.example.expert.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentAdminService {

  private final CommentRepository commentRepository;

  // todo 물리 삭제 아닌가? 왜 물리 삭제로 했을까?
  @Transactional
  public void deleteComment(long commentId) {
    commentRepository.deleteById(commentId);
  }
}