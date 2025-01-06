package org.example.expert.domain.recurring.dto.response;

import java.time.LocalDateTime;
import org.example.expert.common.enums.DayOfWeek;
import org.example.expert.domain.user.dto.response.UserResponseDto;

public record CreateRecurringTodoResponseDto(
    Long id,
    String title,
    String contents,
    String weather,
    String frequency,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    DayOfWeek dayOfWeek,
    int repeatCount,
    UserResponseDto user
) {

}
