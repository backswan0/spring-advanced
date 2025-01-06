package org.example.expert.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateTodoRequestDto(
    @NotEmpty
    String title,

    @NotBlank
    String contents
) {

}