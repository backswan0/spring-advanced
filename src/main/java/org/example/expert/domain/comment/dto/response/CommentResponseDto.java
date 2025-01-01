package org.example.expert.domain.comment.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class CommentResponseDto {

  private final Long id;
  private final String contents;
  private final UserResponse user;

  public CommentResponseDto(
      Long id,
      String contents,
      UserResponse user
  ) {
    this.id = id;
    this.contents = contents;
    this.user = user;
  }
}