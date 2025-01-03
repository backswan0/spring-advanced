package org.example.expert.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.SignInRequestDto;
import org.example.expert.domain.auth.dto.request.SignUpRequestDto;
import org.example.expert.domain.auth.dto.response.SignInResponseDto;
import org.example.expert.domain.auth.dto.response.SignUpResponseDto;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<SignUpResponseDto> signUp(
      @Valid @RequestBody SignUpRequestDto requestDto
  ) {
    SignUpResponseDto responseDto = authService.signUp(requestDto);

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<SignInResponseDto> signIn(
      @Valid @RequestBody SignInRequestDto requestDto
  ) {
    SignInResponseDto responseDto = authService.signIn(requestDto);

    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }
}