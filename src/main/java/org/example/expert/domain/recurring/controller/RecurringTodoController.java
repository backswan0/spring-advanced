package org.example.expert.domain.recurring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.annotation.Auth;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.recurring.dto.request.CreateRecurringTodoRequestDto;
import org.example.expert.domain.recurring.dto.response.CreateRecurringTodoResponseDto;
import org.example.expert.domain.recurring.service.RecurringTodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos/repeated")
@RequiredArgsConstructor
public class RecurringTodoController {

  private final RecurringTodoService recurringTodoService;

  @PostMapping
  public ResponseEntity<CreateRecurringTodoResponseDto> createRecurringTodo(
      @Auth AuthUserDto authUserDto,
      @Valid @RequestBody CreateRecurringTodoRequestDto requestDto
  ) {
    CreateRecurringTodoResponseDto responseDto = recurringTodoService.createRecurringTodo(
        authUserDto,
        requestDto
    );

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }
}