package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.CreateTodoRequestDto;
import org.example.expert.domain.todo.dto.response.CreateTodoResponseDto;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

  @PostMapping("/todos")
  public ResponseEntity<CreateTodoResponseDto> createTodo(
      @Auth AuthUser authUser,
      @Valid @RequestBody CreateTodoRequestDto requestDto
  ) {
    Todo savedTodo = todoService.createTodo(
        authUser,
        requestDto.title(),
        requestDto.contents()
    );

    CreateTodoResponseDto responseDto = new CreateTodoResponseDto(
        savedTodo.getId(),
        savedTodo.getTitle(),
        savedTodo.getContents(),
        savedTodo.getWeather(),
        new UserResponseDto(
            savedTodo.getUser().getId(),
            savedTodo.getUser().getEmail()
        )
    );

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping("/todos")
  public ResponseEntity<Page<TodoResponseDto>> readAllTodos(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(todoService.readAllTodos(page, size));
  }

  @GetMapping("/todos/{todoId}")
  public ResponseEntity<TodoResponseDto> readTodo(
      @PathVariable long todoId
  ) {
    return ResponseEntity.ok(todoService.readTodo(todoId));
  }
}