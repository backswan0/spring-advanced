package org.example.expert.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.enums.AccessLevel;
import org.example.expert.common.exception.ServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

  private static final String BEARER_PREFIX = "Bearer ";
  private static final long TOKEN_TIME = 60 * 60 * 1000L;

  @Value("${jwt.secret.key}")
  private String secretKey;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String createToken(
      Long userId,
      String email,
      AccessLevel accessLevel
  ) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("email", email)
            .claim("accessLevel", accessLevel)
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  public String substringToken(String tokenValue) {

    boolean isValidToken = StringUtils.hasText(tokenValue)
        && tokenValue.startsWith(BEARER_PREFIX);

    if (isValidToken) {
      return tokenValue.substring(7);
    }
    throw new ServerException("Token is not found");
  }

  public Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}