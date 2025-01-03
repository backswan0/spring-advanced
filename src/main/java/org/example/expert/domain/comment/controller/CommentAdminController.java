package org.example.expert.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentAdminController {

  private final CommentAdminService commentAdminService;

  @DeleteMapping("/admin/comments/{commentId}")
  public ResponseEntity<Void> deleteComment(
      // todo 삭제할 권한 검증 시 @Auth AuthUser authUser 사용 필요
      @PathVariable long commentId
  ) {
    // todo 삭제할 권한이 있는지 검증 필요
    commentAdminService.deleteComment(commentId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}