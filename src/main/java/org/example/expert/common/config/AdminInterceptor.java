package org.example.expert.common.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.enums.AccessLevel;
import org.example.expert.common.exception.ForbiddenException;
import org.example.expert.common.jwt.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

  private final JwtUtil jwt;

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler
  ) {

    // Authorization 헤더에서 JWT 토큰 추출
    String jwtHeader = request.getHeader("Authorization");
    String token = jwtHeader.substring(7);

    // JWT 토큰에서 claims 추출
    Claims claims = jwt.extractClaims(token);

    // 액세스 레벨 확인
    String level = claims.get("accessLevel", String.class);
    AccessLevel accessLevel = AccessLevel.of(level);

    boolean isNotAdmin = !AccessLevel.ADMIN.equals(accessLevel);

    // 관리자 권한이 아니면 예외 발생
    if (isNotAdmin) {
      throw new ForbiddenException("Admin access level is required");
    }

    // 관리자 접근 로그 기록
    AdminLogger.log(request.getRequestURL().toString());

    return true;
  }
}