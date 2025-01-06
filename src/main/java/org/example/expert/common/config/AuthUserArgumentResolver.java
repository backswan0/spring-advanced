package org.example.expert.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.enums.AccessLevel;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.auth.exception.AuthException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasAuthAnnotation = parameter
        .getParameterAnnotation(Auth.class) != null;

    boolean isAuthUserType = parameter.getParameterType()
        .equals(AuthUserDto.class);

    // @Auth 어노테이션과 AuthUser 타입이 함께 사용되지 않으면 예외 발생
    boolean isAuthTypeMismatch = hasAuthAnnotation != isAuthUserType;

    if (isAuthTypeMismatch) {
      throw new AuthException("@Auth and AuthUser types must be used together");
    }

    return hasAuthAnnotation;
  }

  @Override
  public Object resolveArgument(
      @Nullable MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory
  ) {
    HttpServletRequest request = (HttpServletRequest) webRequest
        .getNativeRequest();

    Long userId = (Long) request.getAttribute("userId");
    String email = (String) request.getAttribute("email");

    AccessLevel accessLevel = AccessLevel.of((String) request
        .getAttribute("userRole"));

    return new AuthUserDto(userId, email, accessLevel);
  }
}
