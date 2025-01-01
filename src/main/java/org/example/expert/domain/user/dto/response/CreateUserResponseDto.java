package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class CreateUserResponseDto {

  private final String bearerToken;

  public CreateUserResponseDto(String bearerToken) {
    this.bearerToken = bearerToken;
  }
}