package org.example.expert.domain.manager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Manager;
import org.example.expert.domain.common.entity.Todo;
import org.example.expert.domain.common.entity.User;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.CreateManagerRequestDto;
import org.example.expert.domain.manager.dto.response.CreateManagerResponseDto;
import org.example.expert.domain.manager.dto.response.ManagerResponseDto;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.enums.AccessLevel;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

  @InjectMocks
  private ManagerService managerService;

  @Mock
  private ManagerRepository managerRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private TodoRepository todoRepository;


  @Test
  public void manager_목록_조회_시_Todo가_없다면_InvalidRequestException_에러를_던진다() {
    // given
    long todoId = 1L;
    given(todoRepository.findById(todoId)).willReturn(Optional.empty());

    // when
    InvalidRequestException exception = assertThrows(InvalidRequestException.class,
        () -> managerService.readAllManagers(todoId));

    // then
    assertEquals("Todo is not found", exception.getMessage());
  }

  @Test
  void todo의_user가_null인_경우_예외가_발생한다() {
    // given
    AuthUser authUser = new AuthUser(
        1L,
        "a@a.com",
        AccessLevel.USER
    );

    long todoId = 1L;
    long managerId = 2L;

    Todo todo = new Todo();

    ReflectionTestUtils.setField(
        todo,
        "user",
        null
    );

    CreateManagerRequestDto requestDto = new CreateManagerRequestDto(managerId);

    given(todoRepository.findById(todoId)).
        willReturn(Optional.of(todo)
        );

    // when
    InvalidRequestException exception = assertThrows(InvalidRequestException.class,
        () -> managerService.createManager(
            authUser,
            todoId,
            requestDto
        )
    );

    // then
    assertEquals(
        "User who created todo is invalid",
        exception.getMessage()
    );
  }

  @Test // 테스트코드 샘플
  public void manager_목록_조회에_성공한다() {
    // given
    long todoId = 1L;
    User user = new User("user1@example.com", "password", AccessLevel.USER);
    Todo todo = new Todo("Title", "Contents", "Sunny", user);
    ReflectionTestUtils.setField(todo, "id", todoId);

    Manager mockManager = new Manager(todo.getUser(), todo);
    List<Manager> managerList = List.of(mockManager);

    given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
    given(managerRepository.findAllByTodoId(todoId)).willReturn(managerList);

    // when

    List<ManagerResponseDto> managerResponsDtos = managerService.readAllManagers(todoId);

    // then
    assertEquals(1, managerResponsDtos.size());
    assertEquals(mockManager.getId(), managerResponsDtos.get(0).id());
    assertEquals(mockManager.getUser().getEmail(), managerResponsDtos.get(0).user().email());
  }

  @Test
    // 테스트코드 샘플
  void todo가_정상적으로_등록된다() {
    // given
    AuthUser authUser = new AuthUser(1L, "a@a.com", AccessLevel.USER);
    User user = User.fromAuthUser(authUser);  // 일정을 만든 유저

    long todoId = 1L;
    Todo todo = new Todo("Test Title", "Test Contents", "Sunny", user);

    long managerUserId = 2L;
    User managerUser = new User("b@b.com", "password", AccessLevel.USER);  // 매니저로 등록할 유저
    ReflectionTestUtils.setField(managerUser, "id", managerUserId);

    CreateManagerRequestDto createManagerRequestDto = new CreateManagerRequestDto(
        managerUserId); // request dto 생성

    given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
    given(userRepository.findById(managerUserId)).willReturn(Optional.of(managerUser));
    given(managerRepository.save(any(Manager.class))).willAnswer(
        invocation -> invocation.getArgument(0));

    // when
    CreateManagerResponseDto responseDto = managerService.createManager(authUser, todoId,
        createManagerRequestDto);

    CreateManagerResponseDto response = new CreateManagerResponseDto(
        responseDto.id(),
        new UserResponseDto(
            responseDto.user().id(),
            responseDto.user().email()
        )
    );

    // then
    assertNotNull(response);
    assertEquals(managerUser.getId(), response.user().id());
    assertEquals(managerUser.getEmail(), response.user().email());
  }
}
