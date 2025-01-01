package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.CreateTodoRequestDto;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.dto.response.CreateTodoResponseDto;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

  @PostMapping("/todos")
  public ResponseEntity<CreateTodoResponseDto> createTodo(
      @Auth AuthUser authUser,
      @Valid @RequestBody CreateTodoRequestDto requestDto
  ) {
    return ResponseEntity.ok(todoService.createTodo(authUser, requestDto));
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