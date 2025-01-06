package org.example.expert.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import org.example.expert.common.enums.DayOfWeek;

@Getter
@Entity
@Table(name = "recurring_todos")
public class RecurringTodo extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String contents;

  private String weather;

  private String frequency;

  private LocalDateTime startedAt;

  private LocalDateTime endedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false
  )
  private User user;

  private int repeatCount;

  @Enumerated(EnumType.STRING)
  private DayOfWeek dayOfWeek;

  protected RecurringTodo() {
  }

  // 생성자
  public RecurringTodo(
      String title,
      String contents,
      String weather,
      User user,
      String frequency,
      LocalDateTime startedAt,
      LocalDateTime endedAt,
      DayOfWeek dayOfWeek,
      int repeatCount
  ) {
    this.title = title;
    this.contents = contents;
    this.weather = weather;
    this.user = user;
    this.frequency = frequency;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.dayOfWeek = dayOfWeek;
    this.repeatCount = repeatCount;
  }
}