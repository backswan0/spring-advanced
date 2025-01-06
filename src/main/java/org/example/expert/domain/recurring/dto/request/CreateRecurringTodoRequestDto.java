package org.example.expert.domain.recurring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateRecurringTodoRequestDto(
    @NotBlank
    String title,

    @NotBlank
    String contents,

    @NotNull
    LocalDateTime startedAt,

    @NotNull
    LocalDateTime endedAt,

    @NotNull
    String dayOfWeek,

    @NotNull
    int repeatCount
) {

}