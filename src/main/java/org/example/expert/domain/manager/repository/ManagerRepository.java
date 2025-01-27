package org.example.expert.domain.manager.repository;

import java.util.List;
import org.example.expert.common.entity.Manager;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

  @EntityGraph(attributePaths = {"user", "todo"})
  List<Manager> findAllByTodoId(Long todoId);
}