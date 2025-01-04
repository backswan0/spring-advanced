package org.example.expert.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "managers")
public class Manager {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false
  )
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "todo_id",
      nullable = false
  )
  private Todo todo;

  protected Manager() {
  }

  public Manager(
      User user,
      Todo todo
  ) {
    this.user = user;
    this.todo = todo;
  }
}