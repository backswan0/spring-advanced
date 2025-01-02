package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UpdateAccessLevelRequestDto;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.AccessLevel;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

  private final UserRepository userRepository;

  @Transactional
  public void updateUserRole(
      long userId,
      UpdateAccessLevelRequestDto requestDto
  ) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(
            () -> new InvalidRequestException("User not found")
        );

    user.updateAccessLevel(AccessLevel.of(requestDto.accessLevel()));
  }
}