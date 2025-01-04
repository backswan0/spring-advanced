package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.annotation.Auth;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.user.dto.request.UpdatePasswordRequestDto;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDto> readUserById(
      @PathVariable long userId
  ) {
    UserResponseDto responseDto = userService.readUserById(userId);

    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<Void> updatePassword(
      @Auth AuthUserDto authUserDto,
      @RequestBody UpdatePasswordRequestDto requestDto
  ) {
    userService.updatePassword(
        authUserDto,
        requestDto
    );

    return new ResponseEntity<>(HttpStatus.OK);
  }
}