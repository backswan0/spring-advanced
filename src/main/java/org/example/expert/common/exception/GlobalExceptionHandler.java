package org.example.expert.common.exception;

import java.util.HashMap;
import java.util.Map;
import org.example.expert.domain.auth.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 처리: 잘못된 요청에 대한 예외 (404 Not Found)
  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<Map<String, Object>> invalidRequestExceptionException(
      InvalidRequestException ex
  ) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    return getErrorResponse(status, ex.getMessage());
  }

  // 처리: 인증 관련 예외 (401 Unauthorized)
  @ExceptionHandler(AuthException.class)
  public ResponseEntity<Map<String, Object>> handleAuthException(
      AuthException ex
  ) {
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    return getErrorResponse(status, ex.getMessage());
  }

  // 처리: 서버 예외 (500 Internal Server Error)
  @ExceptionHandler(ServerException.class)
  public ResponseEntity<Map<String, Object>> handleServerException(
      ServerException ex
  ) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    return getErrorResponse(status, ex.getMessage());
  }

  // 처리: 접근 금지 예외 (403 Forbidden)
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<Map<String, Object>> handleForbiddenException(
      ForbiddenException ex
  ) {
    HttpStatus status = HttpStatus.FORBIDDEN;
    return getErrorResponse(status, ex.getMessage());
  }

  // 에러 응답을 생성하고 HTTP 상태 코드와 함께 반환
  public ResponseEntity<Map<String, Object>> getErrorResponse(
      HttpStatus status,
      String message
  ) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", status.name());
    errorResponse.put("code", status.value());
    errorResponse.put("message", message);

    return new ResponseEntity<>(errorResponse, status);
  }
}