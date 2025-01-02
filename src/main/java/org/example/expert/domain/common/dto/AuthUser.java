package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.AccessLevel;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final AccessLevel accessLevel;

    public AuthUser(Long id, String email, AccessLevel accessLevel) {
        this.id = id;
        this.email = email;
        this.accessLevel = accessLevel;
    }
}
