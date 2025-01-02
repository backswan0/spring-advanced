package org.example.expert.domain.todo.dto.response;

import org.example.expert.domain.user.dto.response.UserResponseDto;

public record CreateTodoResponseDto(
    Long id,
    String title,
    String contents,
    String weather,
    UserResponseDto user
) {

}