package org.example.expert.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.AccessLevel;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private AccessLevel accessLevel;

  public User(
      String email,
      String password,
      AccessLevel accessLevel
  ) {
    this.email = email;
    this.password = password;
    this.accessLevel = accessLevel;
  }

  private User(
      Long id,
      String email,
      AccessLevel accessLevel
  ) {
    this.id = id;
    this.email = email;
    this.accessLevel = accessLevel;
  }

  public static User fromAuthUser(
      AuthUser authUser
  ) {
    return new User(
        authUser.id(),
        authUser.email(),
        authUser.accessLevel()
    );
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void updateAccessLevel(AccessLevel accessLevel) {
    this.accessLevel = accessLevel;
  }
}