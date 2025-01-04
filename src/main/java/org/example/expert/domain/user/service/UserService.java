package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.config.PasswordEncoder;
import org.example.expert.common.entity.User;
import org.example.expert.common.exception.InvalidRequestException;
import org.example.expert.common.util.EntityFinderUtil;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.user.dto.request.UpdatePasswordRequestDto;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public UserResponseDto readUserById(long userId) {

    User foundUser = EntityFinderUtil.findEntityById(
        userRepository,
        userId,
        User.class
    );

    return new UserResponseDto(
        foundUser.getId(),
        foundUser.getEmail()
    );
  }

  @Transactional
  public void updatePassword(
      AuthUserDto authUserDto,
      UpdatePasswordRequestDto requestDto
  ) {

    if (requestDto.newPassword().length() < 8) {
      throw new InvalidRequestException(
          "New password must be more than 8"
      );
    }

    boolean lacksDigit = !requestDto.newPassword().matches(".*\\d.*");

    if (lacksDigit) {
      throw new InvalidRequestException(
          "New password must include digit"
      );
    }

    boolean lacksCapitalLetter = !requestDto.newPassword().matches(".*[A-Z].*");

    if (lacksCapitalLetter) {
      throw new InvalidRequestException(
          "New password must include capital letter"
      );
    }

    User foundUser = EntityFinderUtil.findEntityById(
        userRepository,
        authUserDto.id(),
        User.class
    );

    boolean isPasswordSame = passwordEncoder.matches(
        requestDto.newPassword(),
        foundUser.getPassword()
    );

    if (isPasswordSame) {
      throw new InvalidRequestException(
          "New password must differ from current password"
      );
    }

    boolean isPasswordDifferent = !passwordEncoder.matches(
        requestDto.oldPassword(),
        foundUser.getPassword()
    );

    if (isPasswordDifferent) {
      throw new InvalidRequestException(
          "Password does not match"
      );
    }

    foundUser.updatePassword(passwordEncoder.encode(
            requestDto.newPassword()
        )
    );
  }
}