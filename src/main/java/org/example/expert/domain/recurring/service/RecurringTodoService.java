package org.example.expert.domain.recurring.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.entity.RecurringTodo;
import org.example.expert.common.entity.User;
import org.example.expert.common.enums.DayOfWeek;
import org.example.expert.common.weather.WeatherClient;
import org.example.expert.domain.auth.dto.AuthUserDto;
import org.example.expert.domain.recurring.dto.request.CreateRecurringTodoRequestDto;
import org.example.expert.domain.recurring.dto.response.CreateRecurringTodoResponseDto;
import org.example.expert.domain.recurring.repository.RecurringTodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecurringTodoService {

  private final RecurringTodoRepository recurringTodoRepository;
  private final WeatherClient weatherClient;

  @Transactional
  public CreateRecurringTodoResponseDto createRecurringTodo(
      AuthUserDto authUserDto,
      CreateRecurringTodoRequestDto requestDto
  ) {
    User userFromAuth = User.fromAuthUser(authUserDto);

    String foundWeather = weatherClient.getTodayWeather();

    DayOfWeek dayOfWeek = DayOfWeek.of(requestDto.dayOfWeek());

    String frequency = "WEEKLY";

    if (dayOfWeek == DayOfWeek.DAILY) {
      frequency = "DAILY";
    }

    RecurringTodo recurringTodo = new RecurringTodo(
        requestDto.title(),
        requestDto.contents(),
        foundWeather,
        userFromAuth,
        frequency,
        requestDto.startedAt(),
        requestDto.endedAt(),
        dayOfWeek,
        requestDto.repeatCount()
    );

    RecurringTodo savedRecurringTodo = recurringTodoRepository.save(recurringTodo);

    return new CreateRecurringTodoResponseDto(
        savedRecurringTodo.getId(),
        savedRecurringTodo.getTitle(),
        savedRecurringTodo.getContents(),
        savedRecurringTodo.getWeather(),
        savedRecurringTodo.getFrequency(),
        savedRecurringTodo.getStartedAt(),
        savedRecurringTodo.getEndedAt(),
        savedRecurringTodo.getDayOfWeek(),
        savedRecurringTodo.getRepeatCount(),
        new UserResponseDto(
            userFromAuth.getId(),
            userFromAuth.getEmail()
        )
    );
  }
}