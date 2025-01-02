package org.example.expert.domain.comment.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponseDto;

@Getter
public class CreateCommentResponseDto {

  private final Long id;
  private final String contents;
  private final UserResponseDto user;

  public CreateCommentResponseDto(
      Long id,
      String contents,
      UserResponseDto user
  ) {
    this.id = id;
    this.contents = contents;
    this.user = user;
  }
}