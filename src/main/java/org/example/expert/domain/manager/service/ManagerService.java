package org.example.expert.domain.manager.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
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
  public Manager createManager(
      AuthUser authUser,
      long todoId,
      long userId
  ) {

    User userFromAuth = User.fromAuthUser(authUser);
    Todo foundTodo = todoRepository.findById(todoId)
        .orElseThrow(
            () -> new InvalidRequestException("Todo is not found")
        );

    boolean isUserInvalid = !ObjectUtils.nullSafeEquals(
        userFromAuth.getId(),
        foundTodo.getUser().getId()
    );

    if (isUserInvalid) {
      throw new InvalidRequestException("User is invalid");
    }

    User foundUser = userRepository.findById(userId)
        .orElseThrow(
            () -> new InvalidRequestException("User is not found")
        );

    boolean isSelfAssignment = ObjectUtils.nullSafeEquals(
        userFromAuth.getId(),
        foundUser.getId()
    );

    if (isSelfAssignment) {
      throw new InvalidRequestException("Todo creator cannot assign self as manager");
    }

    Manager managerToSave = new Manager(foundUser, foundTodo);

    Manager savedManager = managerRepository.save(managerToSave);

    return savedManager;
  }

  @Transactional(readOnly = true)
  public List<Manager> readAllManagers(
      long todoId
  ) {
    Todo foundTodo = todoRepository.findById(todoId)
        .orElseThrow(
            () -> new InvalidRequestException("Todo is not found")
        );

    List<Manager> managerList = new ArrayList<>();

    managerList = managerRepository
        .findAllByTodoId(foundTodo.getId());

    return managerList;
  }

  @Transactional
  public void deleteManager(
      long userId,
      long todoId,
      long managerId
  ) {
    User foundUser = userRepository.findById(userId)
        .orElseThrow(
            () -> new InvalidRequestException("User is not found")
        );

    Todo foundTodo = todoRepository.findById(todoId)
        .orElseThrow(
            () -> new InvalidRequestException("Todo is not found")
        );

    boolean isInvalidUser = foundTodo.getUser() == null
        || !ObjectUtils.nullSafeEquals(
        foundUser.getId(),
        foundTodo.getUser().getId()
    );

    if (isInvalidUser) {
      throw new InvalidRequestException("User who created todo is invalid");
    }

    Manager foundManager = managerRepository.findById(managerId)
        .orElseThrow(
            () -> new InvalidRequestException("Manager is not found")
        );

    boolean isManagerMismatch = !ObjectUtils.nullSafeEquals(
        foundTodo.getId(),
        foundManager.getTodo().getId()
    );

    if (isManagerMismatch) {
      throw new InvalidRequestException("Manager is not assigned to todo");
    }

    managerRepository.delete(foundManager);
  }
}