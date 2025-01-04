package org.example.expert.common.util;

import org.example.expert.common.exception.InvalidRequestException;
import org.springframework.data.jpa.repository.JpaRepository;

public class EntityFinderUtil {

  public static <T, ID> T findEntityById(
      JpaRepository<T, ID> repository,
      ID id,
      Class<T> entityClass
  ) {
    return repository.findById(id)
        .orElseThrow(
            () -> new InvalidRequestException(
                getEntityName(entityClass) + " is not found")
        );
  }

  private static <T> String getEntityName(Class<T> entityClass) {
    return entityClass.getSimpleName();
  }
}