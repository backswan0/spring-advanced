package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.AccessLevel;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

  private final JwtUtil jwtUtil;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String url = httpRequest.getRequestURI();

    if (url.startsWith("/auth")) {
      chain.doFilter(request, response);
      return;
    }

    String bearerJwt = httpRequest.getHeader("Authorization");

    if (bearerJwt == null) {
      // 토큰이 없으면 400 반환
      httpResponse.sendError(
          HttpServletResponse.SC_BAD_REQUEST,
          "JWT token is required"
      );
      return;
    }

    String jwt = jwtUtil.substringToken(bearerJwt);

    try {
      // JWT 유효성 검사 및 claims 추출
      Claims claims = jwtUtil.extractClaims(jwt);
      if (claims == null) {
        httpResponse.sendError(
            HttpServletResponse.SC_BAD_REQUEST,
            "JWT token is wrong"
        );
        return;
      }

      AccessLevel accessLevel = AccessLevel.valueOf(
          claims.get("accessLevel", String.class)
      );

      httpRequest.setAttribute(
          "userId",
          Long.parseLong(claims.getSubject())
      );
      httpRequest.setAttribute(
          "email",
          claims.get("email")
      );
      httpRequest.setAttribute(
          "userRole",
          claims.get("accessLevel")
      );

      if (url.startsWith("/admin")) {
        // 관리자 권한이 없으면 403 반환
        if (!AccessLevel.ADMIN.equals(accessLevel)) {
          httpResponse.sendError(
              HttpServletResponse.SC_FORBIDDEN,
              "Admin authority is required"
          );
          return;
        }
        chain.doFilter(request, response);
        return;
      }

      chain.doFilter(request, response);
    } catch (SecurityException | MalformedJwtException e) {
      log.error("JWT signature is invalid", e);
      httpResponse.sendError(
          HttpServletResponse.SC_UNAUTHORIZED,
          "JWT signature is invalid"
      );
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired", e);
      httpResponse.sendError(
          HttpServletResponse.SC_UNAUTHORIZED,
          "JWT token is expired"
      );
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported", e);
      httpResponse.sendError(
          HttpServletResponse.SC_BAD_REQUEST,
          "JWT token is unsupported"
      );
    } catch (Exception e) {
      log.error("JWT token is invalid", e);
      httpResponse.sendError(
          HttpServletResponse.SC_BAD_REQUEST,
          "JWT token is invalid"
      );
    }
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
