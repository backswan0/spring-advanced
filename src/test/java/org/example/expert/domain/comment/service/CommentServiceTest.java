package org.example.expert.domain.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.example.expert.domain.comment.dto.request.CreateCommentRequestDto;
import org.example.expert.domain.comment.dto.response.CreateCommentResponseDto;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.common.entity.Comment;
import org.example.expert.common.entity.Todo;
import org.example.expert.common.entity.User;
import org.example.expert.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.common.enums.AccessLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;
  @Mock
  private TodoRepository todoRepository;
  @InjectMocks
  private CommentService commentService;

  @Test
  public void comment_등록_중_할일을_찾지_못해_에러가_발생한다() {
    // given
    long todoId = 1;
    CreateCommentRequestDto requestDto = new CreateCommentRequestDto("contents");
    AuthUserDto authUserDto = new AuthUserDto(
        1L,
        "email",
        AccessLevel.USER
    );

    given(todoRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    // when
    InvalidRequestException exception = assertThrows(InvalidRequestException.class,
        () -> {
          commentService.createComment(
              authUserDto,
              todoId,
              requestDto
          );
        }
    );

    // then
    assertEquals("Todo is not found", exception.getMessage());
  }

  @Test
  public void comment를_정상적으로_등록한다() {
    // given
    long todoId = 1;
    CreateCommentRequestDto request = new CreateCommentRequestDto("contents");
    AuthUserDto authUserDto = new AuthUserDto(1L, "email", AccessLevel.USER);
    User user = User.fromAuthUser(authUserDto);
    Todo todo = new Todo("title", "title", "contents", user);
    Comment comment = new Comment(request.contents(), user, todo);

    given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
    given(commentRepository.save(any())).willReturn(comment);

    // when
    CreateCommentResponseDto result = commentService.createComment(authUserDto, todoId, request);

    CreateCommentResponseDto responseDto = new CreateCommentResponseDto(
        result.id(),
        result.contents(),
        new UserResponseDto(
            result.user().id(),
            result.user().email()
        )
    );

    // then
    assertNotNull(responseDto);
  }
}
