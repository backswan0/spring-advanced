package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public User readUserById(long userId) {
    User foundUser = userRepository.findById(userId)
        .orElseThrow(
            () -> new InvalidRequestException("User not found")
        );

    return foundUser;
  }

  @Transactional
  public void updatePassword(
      long userId,
      String oldPassword,
      String newPassword
  ) {

    if (newPassword.length() < 8) {
      throw new InvalidRequestException("New password must be more than 8");
    }

    boolean lacksDigit = !newPassword.matches(".*\\d.*");

    if (lacksDigit) {
      throw new InvalidRequestException("New password must include digit");
    }

    boolean lacksCapital = !newPassword.matches(".*[A-Z].*");

    if (lacksCapital) {
      throw new InvalidRequestException("New password must include capital letter");
    }

    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new InvalidRequestException("User is not found")
        );

    boolean isPasswordSame = passwordEncoder.matches(
        newPassword,
        user.getPassword()
    );

    if (isPasswordSame) {
      throw new InvalidRequestException("New password must differ from current password");
    }

    boolean isPasswordDifferent = !passwordEncoder.matches(
        oldPassword,
        user.getPassword()
    );

    if (isPasswordDifferent) {
      throw new InvalidRequestException("Password does not match");
    }

    user.updatePassword(passwordEncoder.encode(newPassword));
  }
}