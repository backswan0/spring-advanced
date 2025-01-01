package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class CreateTodoResponseDto {

  private final Long id;
  private final String title;
  private final String contents;
  private final String weather;
  private final UserResponse user;

  public CreateTodoResponseDto(
      Long id,
      String title,
      String contents,
      String weather,
      UserResponse user
  ) {
    this.id = id;
    this.title = title;
    this.contents = contents;
    this.weather = weather;
    this.user = user;
  }
}