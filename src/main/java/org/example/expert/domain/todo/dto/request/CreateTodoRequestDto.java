package org.example.expert.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTodoRequestDto(
    @NotBlank
    String title,

    @NotBlank
    String contents
) {

}