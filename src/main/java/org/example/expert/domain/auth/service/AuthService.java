package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignInRequestDto;
import org.example.expert.domain.auth.dto.request.SignUpRequestDto;
import org.example.expert.domain.auth.dto.response.SignInResponseDto;
import org.example.expert.domain.auth.dto.response.SignUpResponseDto;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Transactional
  public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

    boolean isEmailAlreadyRegistered = userRepository
        .existsByEmail(requestDto.getEmail());

    if (isEmailAlreadyRegistered) {
      throw new InvalidRequestException("이미 존재하는 이메일입니다.");
    }

    String encodedPassword = passwordEncoder
        .encode(requestDto.getPassword());

    UserRole userRole = UserRole.of(requestDto.getUserRole());

    User user = new User(
        requestDto.getEmail(),
        encodedPassword,
        userRole
    );
    User savedUser = userRepository.save(user);

    String bearerToken = jwtUtil.createToken(
        savedUser.getId(),
        savedUser.getEmail(),
        userRole
    );

    return new SignUpResponseDto(bearerToken);
  }

  public SignInResponseDto signIn(SignInRequestDto request) {
    User user = userRepository
        .findByEmail(request.getEmail())
        .orElseThrow(
            () -> new InvalidRequestException("가입되지 않은 유저입니다."));

    boolean isPasswordDifferent = !passwordEncoder
        .matches(
            request.getPassword(),
            user.getPassword()
        );

    if (isPasswordDifferent) {
      throw new AuthException("잘못된 비밀번호입니다.");
    }

    String bearerToken = jwtUtil.createToken(
        user.getId(),
        user.getEmail(),
        user.getUserRole()
    );

    return new SignInResponseDto(bearerToken);
  }
}