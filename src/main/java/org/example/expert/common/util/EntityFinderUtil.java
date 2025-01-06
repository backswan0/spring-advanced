package org.example.expert.common.util;

import org.example.expert.common.exception.InvalidRequestException;
import org.springframework.data.jpa.repository.JpaRepository;

public class EntityFinderUtil {

  // 주어진 ID로 엔티티를 찾아 없으면 예외를 던지는 메서드
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

  // // 엔티티 클래스의 이름 반환
  private static <T> String getEntityName(Class<T> entityClass) {
    return entityClass.getSimpleName();
  }
}