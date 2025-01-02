package org.example.expert.domain.manager.dto.response;

import org.example.expert.domain.user.dto.response.UserResponseDto;

public record ManagerResponseDto(
    Long id,
    UserResponseDto user
) {

}