package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.entity.User;
import org.example.expert.common.enums.AccessLevel;
import org.example.expert.common.util.EntityFinderUtil;
import org.example.expert.domain.user.dto.request.UpdateAccessLevelRequestDto;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

  private final UserRepository userRepository;

  @Transactional
  public void updateAccessLevel(
      long userId,
      UpdateAccessLevelRequestDto requestDto
  ) {
    User foundUser = EntityFinderUtil.findEntityById(
        userRepository,
        userId,
        User.class
    );

    // 요청된 access level을 적용하여 권한 변경
    foundUser.updateAccessLevel(
        AccessLevel.of(
            requestDto.accessLevel()
        )
    );
  }
}