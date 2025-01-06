package org.example.expert.common.enums;

import java.util.Arrays;
import org.example.expert.common.exception.InvalidRequestException;

public enum DayOfWeek {

  DAILY,
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY,
  SATURDAY,
  SUNDAY;

  public static DayOfWeek of(String dayOfWeek) {
    return Arrays.stream(DayOfWeek.values())
        .filter(
            day -> day
                .name()
                .equalsIgnoreCase(dayOfWeek)
        ).findFirst()
        .orElseThrow(
            () -> new InvalidRequestException("Day of week is invalid")
        );
  }
}