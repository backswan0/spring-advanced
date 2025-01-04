package org.example.expert.domain.manager.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.EntityFinderUtil;
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
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ManagerService {

  private final ManagerRepository managerRepository;
  private final UserRepository userRepository;
  private final TodoRepository todoRepository;

  @Transactional
  public CreateManagerResponseDto createManager(
      AuthUser authUser,
      long todoId,
      CreateManagerRequestDto requestDto
  ) {

    User userFromAuth = User.fromAuthUser(authUser);

    Todo foundTodo = EntityFinderUtil.findEntityById(
        todoRepository,
        todoId,
        Todo.class
    );

    if (foundTodo.getUser() == null) {
      throw new InvalidRequestException(
          "User who created todo is invalid"
      );
    }

    boolean isUserInvalid = !ObjectUtils.nullSafeEquals(
        userFromAuth.getId(),
        foundTodo.getUser().getId()
    );

    if (isUserInvalid) {
      throw new InvalidRequestException(
          "User is invalid"
      );
    }

    User foundUser = EntityFinderUtil.findEntityById(
        userRepository,
        requestDto.managerUserId(),
        User.class
    );

    boolean isSelfAssignment = ObjectUtils.nullSafeEquals(
        userFromAuth.getId(),
        foundUser.getId()
    );

    if (isSelfAssignment) {
      throw new InvalidRequestException(
          "Todo creator cannot assign self as manager"
      );
    }

    Manager managerToSave = new Manager(foundUser, foundTodo);

    Manager savedManager = managerRepository.save(managerToSave);

    return new CreateManagerResponseDto(
        savedManager.getId(),
        new UserResponseDto(
            savedManager.getUser().getId(),
            savedManager.getUser().getEmail()
        )
    );
  }

  @Transactional(readOnly = true)
  public List<ManagerResponseDto> readAllManagers(long todoId) {
    Todo foundTodo = EntityFinderUtil.findEntityById(
        todoRepository,
        todoId,
        Todo.class
    );

    List<Manager> managerList = new ArrayList<>();

    managerList = managerRepository.findAllByTodoId(foundTodo.getId());

    List<ManagerResponseDto> managerDtoList = new ArrayList<>();

    managerDtoList = managerList.stream()
        .map(manager -> {
              UserResponseDto responseDto = new UserResponseDto(
                  manager.getUser().getId(),
                  manager.getUser().getEmail()
              );
              return new ManagerResponseDto(
                  manager.getId(),
                  responseDto
              );
            }
        ).toList();

    return managerDtoList;
  }

  @Transactional
  public void deleteManager(
      long userId,
      long todoId,
      long managerId
  ) {
    User foundUser = EntityFinderUtil.findEntityById(
        userRepository,
        userId,
        User.class
    );

    Todo foundTodo = EntityFinderUtil.findEntityById(
        todoRepository,
        todoId,
        Todo.class
    );

    boolean isInvalidUser = foundTodo.getUser() == null
        || !ObjectUtils.nullSafeEquals(
        foundUser.getId(),
        foundTodo.getUser().getId()
    );

    if (isInvalidUser) {
      throw new InvalidRequestException(
          "User who created todo is invalid"
      );
    }

    Manager foundManager = EntityFinderUtil.findEntityById(
        managerRepository,
        managerId,
        Manager.class
    );

    boolean isManagerMismatch = !ObjectUtils.nullSafeEquals(
        foundTodo.getId(),
        foundManager.getTodo().getId()
    );

    if (isManagerMismatch) {
      throw new InvalidRequestException(
          "Manager is not assigned to todo"
      );
    }

    managerRepository.delete(foundManager);
  }
}