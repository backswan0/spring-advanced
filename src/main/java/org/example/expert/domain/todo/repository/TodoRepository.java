package org.example.expert.domain.todo.repository;

import org.example.expert.domain.common.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

// 중복 코드 해결: default 메서드를 써서 해결해보자 (일정을 여러 군데에서 호출하지만, 관리는 여기서만 이루어지니까)
public interface TodoRepository extends JpaRepository<Todo, Long> {

  @EntityGraph(attributePaths = {"user"})
  Page<Todo> findAllByOrderByUpdatedAtDesc(Pageable pageable);

  int countById(Long todoId);
}