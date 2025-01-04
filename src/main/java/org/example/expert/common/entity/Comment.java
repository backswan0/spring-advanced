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
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String contents;

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

  public Comment(
      String contents,
      User user,
      Todo todo
  ) {
    this.contents = contents;
    this.user = user;
    this.todo = todo;
  }

  public void update(String contents) {
    this.contents = contents;
  }
}