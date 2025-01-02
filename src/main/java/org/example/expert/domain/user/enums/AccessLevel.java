package org.example.expert.domain.user.enums;

import java.util.Arrays;
import org.example.expert.domain.common.exception.InvalidRequestException;

/*
todo
 [수정 전] UserRole
 [수정 후] AccessLevel
 [후보] Authority
 [수정 근거] 권한 차이를 명확하게 보여주고자 수정
 */
public enum AccessLevel {
  ADMIN, USER;

  public static AccessLevel of(String accessLevel) {
    return Arrays.stream(AccessLevel.values())
        .filter(
            level -> level
                .name()
                .equalsIgnoreCase(accessLevel)
        ).findFirst()
        .orElseThrow(
            () -> new InvalidRequestException("Access level is invalid")
        );
  }
}