package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.AccessLevel;
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
  public String signUp(
      String email,
      String password,
      String accessLevelString
  ) {
    boolean isEmailAlreadyRegistered = userRepository.findByEmail(email)
        .isPresent();

    if (isEmailAlreadyRegistered) {
      throw new InvalidRequestException("Email is already registered");
    } // todo

    String encodedPassword = passwordEncoder.encode(password);

    AccessLevel accessLevel = AccessLevel.of(accessLevelString);

    User userToSave = new User(
        email,
        encodedPassword,
        accessLevel
    );

    User savedUser = userRepository.save(userToSave);

    String token = jwtUtil.createToken(
        savedUser.getId(),
        savedUser.getEmail(),
        savedUser.getAccessLevel()
    );

    return jwtUtil.substringToken(token);
  }

  public String signIn(
      String email,
      String password
  ) {
    User foundUser = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new InvalidRequestException("User is not registered")
        ); // todo

    boolean isPasswordDifferent = !passwordEncoder.matches(
        password,
        foundUser.getPassword()
    );

    if (isPasswordDifferent) {
      throw new AuthException("Password does not match");
    } // todo

    String token = jwtUtil.createToken(
        foundUser.getId(),
        foundUser.getEmail(),
        foundUser.getAccessLevel()
    );

    return jwtUtil.substringToken(token);
  }
}