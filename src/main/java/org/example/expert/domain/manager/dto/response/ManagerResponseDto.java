package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class ManagerResponseDto {

  private final Long id;
  private final UserResponse user;

  public ManagerResponseDto(
      Long id,
      UserResponse user
  ) {
    this.id = id;
    this.user = user;
  }
}