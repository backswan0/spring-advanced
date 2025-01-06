package org.example.expert.domain.manager.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.jwt.JwtUtil;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.manager.dto.request.CreateManagerRequestDto;
import org.example.expert.domain.manager.dto.response.CreateManagerResponseDto;
import org.example.expert.domain.manager.dto.response.ManagerResponseDto;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos/{todoId}/managers")
@RequiredArgsConstructor
public class ManagerController {

  private final ManagerService managerService;
  private final JwtUtil jwtUtil;

  @PostMapping
  public ResponseEntity<CreateManagerResponseDto> createManager(
      @Auth AuthUserDto authUserDto,
      @PathVariable long todoId,
      @Valid @RequestBody CreateManagerRequestDto requestDto
  ) {

    CreateManagerResponseDto responseDto = managerService.createManager(
        authUserDto,
        todoId,
        requestDto
    );

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<ManagerResponseDto>> readAllManagers(
      @PathVariable long todoId
  ) {

    List<ManagerResponseDto> managerDtoList = new ArrayList<>();

    managerDtoList = managerService.readAllManagers(todoId);

    return new ResponseEntity<>(managerDtoList, HttpStatus.OK);
  }

  @DeleteMapping("/{managerId}")
  public ResponseEntity<Void> deleteManager(
      @RequestHeader("Authorization") String bearerToken,
      @PathVariable long todoId,
      @PathVariable long managerId
  ) {
    Claims claims = jwtUtil.extractClaims(
        bearerToken.substring(7)
    );

    long userId = Long.parseLong(claims.getSubject());

    managerService.deleteManager(
        userId,
        todoId,
        managerId
    );

    return new ResponseEntity<>(HttpStatus.OK);
  }
}