package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class CreateManagerResponseDto {

  private final Long id;
  private final UserResponse user;

  public CreateManagerResponseDto(
      Long id,
      UserResponse user
  ) {
    this.id = id;
    this.user = user;
  }
}