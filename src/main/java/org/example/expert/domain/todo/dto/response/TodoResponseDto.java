package org.example.expert.domain.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import org.example.expert.domain.user.dto.response.UserResponseDto;

public record TodoResponseDto(
    Long id,
    String title,
    String contents,
    String weather,
    UserResponseDto user,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt
) {

}