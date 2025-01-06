package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.entity.Todo;
import org.example.expert.common.entity.User;
import org.example.expert.common.util.EntityFinderUtil;
import org.example.expert.common.weather.WeatherClient;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.recurring.repository.RecurringTodoRepository;
import org.example.expert.domain.todo.dto.request.CreateTodoRequestDto;
import org.example.expert.domain.todo.dto.response.CreateTodoResponseDto;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final RecurringTodoRepository recurringTodoRepository;
  private final WeatherClient weatherClient;

  @Transactional
  public CreateTodoResponseDto createTodo(
      AuthUserDto authUserDto,
      CreateTodoRequestDto requestDto
  ) {
    User userFromAuth = User.fromAuthUser(authUserDto);

    String foundWeather = weatherClient.getTodayWeather();

    Todo todo = new Todo(
        requestDto.title(),
        requestDto.contents(),
        foundWeather,
        userFromAuth
    );

    Todo savedTodo = todoRepository.save(todo);

    return new CreateTodoResponseDto(
        savedTodo.getId(),
        savedTodo.getTitle(),
        savedTodo.getContents(),
        savedTodo.getWeather(),
        new UserResponseDto(
            userFromAuth.getId(),
            userFromAuth.getEmail()
        )
    );
  }

  @Transactional(readOnly = true)
  public Page<TodoResponseDto> readAllTodos(Pageable pageable) {

    Page<Todo> todoPage = todoRepository.findAllByOrderByUpdatedAtDesc(pageable);

    return todoPage.map(
        foundTodo -> new TodoResponseDto(
            foundTodo.getId(),
            foundTodo.getTitle(),
            foundTodo.getContents(),
            foundTodo.getWeather(),
            new UserResponseDto(
                foundTodo.getUser().getId(),
                foundTodo.getUser().getEmail()
            ),
            foundTodo.getCreatedAt(),
            foundTodo.getUpdatedAt()
        )
    );
  }

  @Transactional(readOnly = true)
  public TodoResponseDto readTodoById(long todoId) {

    Todo foundTodo = EntityFinderUtil.findEntityById(
        todoRepository,
        todoId,
        Todo.class
    );

    return new TodoResponseDto(
        foundTodo.getId(),
        foundTodo.getTitle(),
        foundTodo.getContents(),
        foundTodo.getWeather(),
        new UserResponseDto(
            foundTodo.getUser().getId(),
            foundTodo.getUser().getEmail()
        ),
        foundTodo.getCreatedAt(),
        foundTodo.getUpdatedAt()
    );
  }
}