package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.EntityFinderUtil;
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
  public void updateAccessLevel(
      long userId,
      String accessLevel
  ) {
    User foundUser = EntityFinderUtil.findEntityById(
        userRepository,
        userId,
        User.class
    );

    foundUser.updateAccessLevel(AccessLevel.of(accessLevel));
  }
}