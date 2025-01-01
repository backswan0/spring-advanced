package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponseDto;

@Getter
public class CreateManagerResponseDto {

  private final Long id;
  private final UserResponseDto user;

  public CreateManagerResponseDto(
      Long id,
      UserResponseDto user
  ) {
    this.id = id;
    this.user = user;
  }
}