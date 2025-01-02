package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.dto.request.CreateCommentRequestDto;
import org.example.expert.domain.comment.dto.response.CreateCommentResponseDto;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.AccessLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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
        CreateCommentRequestDto request = new CreateCommentRequestDto("contents");
        AuthUser authUser = new AuthUser(1L, "email", AccessLevel.USER);

        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ServerException exception = assertThrows(ServerException.class, () -> {
            commentService.createComment(authUser, todoId, request);
        });

        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @Test
    public void comment를_정상적으로_등록한다() {
        // given
        long todoId = 1;
        CreateCommentRequestDto request = new CreateCommentRequestDto("contents");
        AuthUser authUser = new AuthUser(1L, "email", AccessLevel.USER);
        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo("title", "title", "contents", user);
        Comment comment = new Comment(request.contents(), user, todo);

        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
        given(commentRepository.save(any())).willReturn(comment);

        // when
        CreateCommentResponseDto result = commentService.createComment(authUser, todoId, request);

        // then
        assertNotNull(result);
    }
}
