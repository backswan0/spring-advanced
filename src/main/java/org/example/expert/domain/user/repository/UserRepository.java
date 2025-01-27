package org.example.expert.domain.user.repository;

import java.util.Optional;
import org.example.expert.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
}