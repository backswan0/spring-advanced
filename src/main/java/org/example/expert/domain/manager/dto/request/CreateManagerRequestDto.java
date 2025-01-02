package org.example.expert.domain.manager.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateManagerRequestDto {

  @NotNull
  private Long managerUserId; // 일정 작상자가 배치하는 유저 id
}