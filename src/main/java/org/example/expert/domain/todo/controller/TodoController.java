package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.CreateTodoRequestDto;
import org.example.expert.domain.todo.dto.response.CreateTodoResponseDto;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

  @PostMapping
  public ResponseEntity<CreateTodoResponseDto> createTodo(
      @Auth AuthUser authUser,
      @Valid @RequestBody CreateTodoRequestDto requestDto
  ) {
    CreateTodoResponseDto responseDto = todoService.createTodo(
        authUser,
        requestDto
    );

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Page<TodoResponseDto>> readAllTodos(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    // 음수가 나오지 않도록 Math.max 사용
    int adjustedPage = Math.max(page - 1, 0);
    int adjustedSize = Math.max(size, 1);

    Pageable adjustedPageable = PageRequest.of(
        adjustedPage,
        adjustedSize);

    Page<TodoResponseDto> todoDtoPage = todoService.readAllTodos(adjustedPageable);

    return new ResponseEntity<>(todoDtoPage, HttpStatus.OK);
  }

  @GetMapping("/{todoId}")
  public ResponseEntity<TodoResponseDto> readTodoById(
      @PathVariable long todoId
  ) {
    TodoResponseDto responseDto = todoService.readTodoById(todoId);

    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }
}

/*
계층을 이동할 때 사용하는 dto
컨트롤러에서 엔티티를 가져버리면 계층 이동이 필요한가? dto를 사용하는 의미가 없어진다.
외부에서 들어온 값이 유효한지 검증 + 가공
request dto를 전달만 하자. dto에서 무엇을 쓸지는 서비스의 역할

Pageable adjustedPageable = PageRequest.of(page - 1, size);
-> 컨트롤러에 있어야 한다. 외부에서 들어온 값을 전처리해주는 단계니까
 */