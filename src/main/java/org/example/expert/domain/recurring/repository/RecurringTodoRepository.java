package org.example.expert.domain.recurring.repository;

import org.example.expert.common.entity.RecurringTodo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringTodoRepository extends JpaRepository<RecurringTodo, Long> {

}
