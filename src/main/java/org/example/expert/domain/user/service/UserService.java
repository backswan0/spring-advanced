package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UpdatePasswordRequestDto;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserResponseDto readUserById(long userId) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(
            () -> new InvalidRequestException("User not found")
        );

    return new UserResponseDto(user.getId(), user.getEmail());
  }

  @Transactional
  public void updatePassword(
      long userId,
      UpdatePasswordRequestDto requestDto
  ) {
    if (requestDto.getNewPassword().length() < 8 ||
        !requestDto.getNewPassword().matches(".*\\d.*") ||
        !requestDto.getNewPassword().matches(".*[A-Z].*")) {
      throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new InvalidRequestException("User not found"));

    if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
      throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
    }

    if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
      throw new InvalidRequestException("잘못된 비밀번호입니다.");
    }

    user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
  }
}