package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequestDto(
    @NotBlank
    String oldPassword,

    @NotBlank
    String newPassword
) {

}