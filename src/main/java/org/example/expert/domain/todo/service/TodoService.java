package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final WeatherClient weatherClient;

  @Transactional
  public Todo createTodo(
      AuthUser authUser,
      String title,
      String contents
  ) {
    User user = User.fromAuthUser(authUser);
    String weather = weatherClient.getTodayWeather();

    Todo todo = new Todo(
        title,
        contents,
        weather,
        user
    );

    Todo savedTodo = todoRepository.save(todo);

    return savedTodo;
  }

  @Transactional(readOnly = true)
  public Page<Todo> readAllTodos(
      int page,
      int size
  ) {
    Pageable pageable = PageRequest
        .of(page - 1, size);

    Page<Todo> todoPage = todoRepository
        .findAllByOrderByUpdatedAtDesc(pageable);

    return todoPage;
  }

  @Transactional(readOnly = true)
  public Todo readTodoById(long todoId) {

    Todo foundTodo = todoRepository
        .findById(todoId)
        .orElseThrow(
            () -> new InvalidRequestException("Todo is not found")
        ); // todo

    return foundTodo;
  }
}