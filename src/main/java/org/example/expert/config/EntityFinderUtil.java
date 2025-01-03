package org.example.expert.config;

import java.util.Optional;
import org.example.expert.domain.common.exception.InvalidRequestException;

public class EntityFinderUtil {

  // 레포지토리를 넘겨보도록, 옵셔널 대신에
  public static <T> T findEntityById(Optional<T> entityOptional, Class<T> entityClass) {

    return entityOptional.orElseThrow(
        () -> new InvalidRequestException(
            getEntityName(entityClass) + " is not found")
    );
  }

  private static <T> String getEntityName(Class<T> entityClass) {
    return entityClass.getSimpleName();
  }
}