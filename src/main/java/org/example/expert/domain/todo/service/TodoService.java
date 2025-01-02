package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    return todoRepository.save(todo);
  }

  public Page<TodoResponseDto> readAllTodos(
      int page,
      int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size);

    Page<Todo> todos = todoRepository
        .findAllByOrderByModifiedAtDesc(pageable);

    return todos.map(todo -> new TodoResponseDto(
            todo.getId(),
            todo.getTitle(),
            todo.getContents(),
            todo.getWeather(),
            new UserResponseDto(todo.getUser().getId(), todo.getUser().getEmail()),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        )
    );
  }

  public TodoResponseDto readTodo(long todoId) {
    Todo todo = todoRepository
        .findById(todoId)
        .orElseThrow(
            () -> new InvalidRequestException("Todo not found")
        );

    User user = todo.getUser();

    return new TodoResponseDto(
        todo.getId(),
        todo.getTitle(),
        todo.getContents(),
        todo.getWeather(),
        new UserResponseDto(user.getId(), user.getEmail()),
        todo.getCreatedAt(),
        todo.getUpdatedAt()
    );
  }
}