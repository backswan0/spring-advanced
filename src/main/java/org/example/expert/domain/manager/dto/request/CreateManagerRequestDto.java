package org.example.expert.domain.manager.dto.request;

import jakarta.validation.constraints.NotNull;


public record CreateManagerRequestDto(
    @NotNull
    Long managerUserId
) {

}