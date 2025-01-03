package org.example.expert.domain.comment.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.request.CreateCommentRequestDto;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.dto.response.CreateCommentResponseDto;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos/{todoId}/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<CreateCommentResponseDto> createComment(
      @Auth AuthUser authUser,
      @PathVariable long todoId,
      @Valid @RequestBody CreateCommentRequestDto requestDto
  ) {
    CreateCommentResponseDto savedComment = commentService.createComment(
        authUser,
        todoId,
        requestDto
    );

    return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<CommentResponseDto>> readAllComments(
      @PathVariable long todoId
  ) {
    List<CommentResponseDto> commentDtoList = new ArrayList<>();

    commentDtoList = commentService.readAllComments(todoId);

    return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
  }
  // todo 자기 것도 수정할 수 있어야 하니까 api 필요(update / delete)
}