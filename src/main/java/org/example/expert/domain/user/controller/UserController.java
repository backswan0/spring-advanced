package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UpdatePasswordRequestDto;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/users/{userId}")
  public ResponseEntity<UserResponseDto> findUserById(
      @PathVariable long userId
  ) {
    return ResponseEntity.ok(userService.readUserById(userId));
  }

  @PutMapping("/users")
  public void updatePassword(
      @Auth AuthUser authUser,
      @RequestBody UpdatePasswordRequestDto requestDto
  ) {
    userService.updatePassword(
        authUser.id(),
        requestDto
    );
  }
}