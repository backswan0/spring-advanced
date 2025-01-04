package org.example.expert.domain.auth.dto;

import org.example.expert.common.enums.AccessLevel;

public record AuthUserDto(
    Long id,
    String email,
    AccessLevel accessLevel
) {

}