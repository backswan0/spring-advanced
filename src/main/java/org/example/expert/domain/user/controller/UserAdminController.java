package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.request.UpdateAccessLevelRequestDto;
import org.example.expert.domain.user.service.UserAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAdminController {

  private final UserAdminService userAdminService;

  @PatchMapping("/admin/users/{userId}")
  public ResponseEntity<Void> updateAccessLevel(
      @PathVariable long userId,
      @RequestBody UpdateAccessLevelRequestDto requestDto
  ) {
    userAdminService.updateAccessLevel(
        userId,
        requestDto.accessLevel()
    );

    return new ResponseEntity<>(HttpStatus.OK);
  }
}