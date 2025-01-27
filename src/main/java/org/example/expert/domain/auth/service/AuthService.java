package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.config.PasswordEncoder;
import org.example.expert.common.entity.User;
import org.example.expert.common.enums.AccessLevel;
import org.example.expert.common.exception.InvalidRequestException;
import org.example.expert.common.jwt.JwtUtil;
import org.example.expert.domain.auth.dto.request.SignInRequestDto;
import org.example.expert.domain.auth.dto.request.SignUpRequestDto;
import org.example.expert.domain.auth.dto.response.SignInResponseDto;
import org.example.expert.domain.auth.dto.response.SignUpResponseDto;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Transactional
  public SignUpResponseDto signUp(
      SignUpRequestDto requestDto

  ) {
    boolean isAlreadyRegistered = userRepository.findByEmail(requestDto.email())
        .isPresent();

    if (isAlreadyRegistered) {
      throw new InvalidRequestException("Email is already registered");
    }

    String encodedPassword = passwordEncoder.encode(requestDto.password());

    AccessLevel accessLevel = AccessLevel.of(requestDto.accessLevel());

    User userToSave = new User(
        requestDto.email(),
        encodedPassword,
        accessLevel
    );

    User savedUser = userRepository.save(userToSave);

    String token = jwtUtil.createToken(
        savedUser.getId(),
        savedUser.getEmail(),
        savedUser.getAccessLevel()
    );

    return new SignUpResponseDto(jwtUtil.substringToken(token));
  }

  public SignInResponseDto signIn(
      SignInRequestDto requestDto
  ) {
    User foundUser = userRepository.findByEmail(requestDto.email())
        .orElseThrow(
            () -> new InvalidRequestException("User is not registered")
        );

    boolean isPasswordDifferent = !passwordEncoder.matches(
        requestDto.password(),
        foundUser.getPassword()
    );

    if (isPasswordDifferent) {
      throw new AuthException("Password does not match");
    }

    // JWT 토큰 생성
    String token = jwtUtil.createToken(
        foundUser.getId(),
        foundUser.getEmail(),
        foundUser.getAccessLevel()
    );

    return new SignInResponseDto(jwtUtil.substringToken(token));
  }
}