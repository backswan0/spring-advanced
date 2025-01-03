package org.example.expert.config;

import java.util.Optional;
import org.example.expert.domain.common.exception.InvalidRequestException;

public class EntityFinderUtil {

  public static <T> T findEntityById(Optional<T> entityOptional) {

    return entityOptional.orElseThrow(
        () -> new InvalidRequestException(getEntityName(entityOptional) + " is not found"));
  }

  private static <T> String getEntityName(Optional<T> entityOptional) {
    return entityOptional
        .map(entity -> entity.getClass().getSimpleName())  // 엔티티 이름 추출
        .orElse("Entity");
  }
}