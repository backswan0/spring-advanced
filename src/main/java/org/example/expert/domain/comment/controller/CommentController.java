package org.example.expert.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.request.CreateCommentRequestDto;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.dto.response.CreateCommentResponseDto;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/todos/{todoId}/comments")
  public ResponseEntity<CreateCommentResponseDto> createComment(
      @Auth AuthUser authUser,
      @PathVariable long todoId,
      @Valid @RequestBody CreateCommentRequestDto requestDto
  ) {
    CreateCommentResponseDto responseDto = commentService
        .createComment(
            authUser,
            todoId,
            requestDto
        );

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping("/todos/{todoId}/comments")
  public ResponseEntity<List<CommentResponseDto>> readAllComments(
      @PathVariable long todoId
  ) {

    List<CommentResponseDto> responseDtoList = commentService
        .readAllComments(todoId);

    return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
  }
}