package org.example.expert.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.enums.AccessLevel;
import org.example.expert.domain.auth.dto.AuthUserDto;
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
      @Auth AuthUserDto authUserDto,
      @PathVariable long commentId
  ) {

    boolean isNotAdminUser = !authUserDto.accessLevel().
        equals(AccessLevel.ADMIN);

    if (isNotAdminUser) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    commentAdminService.deleteComment(commentId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}