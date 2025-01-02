package org.example.expert.domain.comment.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.request.CreateCommentRequestDto;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.dto.response.CreateCommentResponseDto;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponseDto;
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
    Comment savedComment = commentService
        .createComment(
            authUser,
            todoId,
            requestDto.contents()
        );

    CreateCommentResponseDto responseDto = new CreateCommentResponseDto(
        savedComment.getId(),
        savedComment.getContents(),
        new UserResponseDto(
            savedComment.getUser().getId(),
            savedComment.getUser().getEmail()
        )
    );

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<CommentResponseDto>> readAllComments(
      @PathVariable long todoId
  ) {
    List<Comment> commentList = new ArrayList<>();

    commentList = commentService.readAllComments(todoId);

    List<CommentResponseDto> commentDtoList = new ArrayList<>();

    commentDtoList = commentList.stream()
        .map(comment -> {
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

    return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
  }
  // 자기 것도 수정할 수는 있어야 하니까 api 추가를 해야 한다. update / delete
}