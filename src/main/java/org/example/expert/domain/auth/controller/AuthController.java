package org.example.expert.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.SignInRequestDto;
import org.example.expert.domain.auth.dto.request.SignUpRequestDto;
import org.example.expert.domain.auth.dto.response.SignInResponseDto;
import org.example.expert.domain.auth.dto.response.SignUpResponseDto;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth/sign-up")
  public SignUpResponseDto signUp(
      @Valid @RequestBody SignUpRequestDto requestDto
  ) {
    String bearerToken = authService.signUp(
        requestDto.email(),
        requestDto.password(),
        requestDto.accessLevel()
    );

    return new SignUpResponseDto(bearerToken);
  }

  @PostMapping("/auth/sign-in")
  public SignInResponseDto signIn(
      @Valid @RequestBody SignInRequestDto requestDto
  ) {
    String bearerToken = authService.signIn(
        requestDto.email(),
        requestDto.password()
    );

    return new SignInResponseDto(bearerToken);
  }
}