package org.example.expert.domain.manager.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.CreateManagerRequestDto;
import org.example.expert.domain.manager.dto.response.CreateManagerResponseDto;
import org.example.expert.domain.manager.dto.response.ManagerResponseDto;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.user.dto.response.UserResponseDto;
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
      @Auth AuthUser authUser,
      @PathVariable long todoId,
      @Valid @RequestBody CreateManagerRequestDto requestDto
  ) {
    Manager foundManager = managerService
        .createManager(
            authUser,
            todoId,
            requestDto.managerUserId()
        );

    CreateManagerResponseDto responseDto = new CreateManagerResponseDto(
        foundManager.getId(),
        new UserResponseDto(
            foundManager.getUser().getId(),
            foundManager.getUser().getEmail()
        )
    );

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<ManagerResponseDto>> readAllManagers(
      @PathVariable long todoId // todo id 확인 필요
  ) {

    List<Manager> managerList = new ArrayList<>();

    managerList = managerService.readAllManagers(todoId);

    List<ManagerResponseDto> managerDtoList = new ArrayList<>();

    managerDtoList = managerList.stream()
        .map(manager -> {
              UserResponseDto responseDto = new UserResponseDto(
                  manager.getUser().getId(),
                  manager.getUser().getEmail()
              );
              return new ManagerResponseDto(
                  manager.getId(),
                  responseDto
              );
            }
        ).toList();

    return new ResponseEntity<>(managerDtoList, HttpStatus.OK);
  }

  @DeleteMapping("/{managerId}")
  public ResponseEntity<Void> deleteManager(
      @RequestHeader("Authorization") String bearerToken,
      @PathVariable long todoId,
      @PathVariable long managerId
  ) {
    Claims claims = jwtUtil.extractClaims(bearerToken.substring(7));

    long userId = Long.parseLong(claims.getSubject());

    managerService.deleteManager(
        userId,
        todoId,
        managerId
    );

    return new ResponseEntity<>(HttpStatus.OK);
  }
}