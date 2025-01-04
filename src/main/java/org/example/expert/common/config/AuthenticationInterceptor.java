package org.example.expert.common.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.enums.AccessLevel;
import org.example.expert.common.jwt.JwtUtil;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

  private final JwtUtil jwt;
  private final UserRepository userRepository;

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler
  ) throws Exception {

    String jwtHeader = request.getHeader("Authorization");

    boolean isInvalidToken = jwtHeader == null
        || !jwtHeader.startsWith("Bearer ");

    if (isInvalidToken) {
      response.sendError(
          HttpServletResponse.SC_UNAUTHORIZED,
          "JWT bearer Token is required"
      );
      return false;
    }

    String token = jwtHeader.substring(7);

    Claims claims = jwt.extractClaims(token);

    String level = claims.get("accessLevel", String.class);

    AccessLevel accessLevel = AccessLevel.of(level);

    boolean isNotAdmin = !AccessLevel.ADMIN.equals(accessLevel);

    if (isNotAdmin) {
      response.sendError(
          HttpServletResponse.SC_FORBIDDEN,
          "Admin access level is required"
      );
      return false;
    }

    // 로그 기록 시 마이크로초 제거
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String formattedDate = now.format(formatter);


    log.info(
        "Admin access: URL = {}, Time: {}",
        request.getRequestURL(),
        formattedDate
    );

    return true;
  }
}