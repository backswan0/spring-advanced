package org.example.expert.domain.common.dto;

import org.example.expert.domain.user.enums.AccessLevel;

public record AuthUser(
    Long id,
    String email,
    AccessLevel accessLevel
) {

}