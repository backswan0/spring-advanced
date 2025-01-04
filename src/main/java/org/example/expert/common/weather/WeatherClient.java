package org.example.expert.common.weather;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.example.expert.common.exception.ServerException;
import org.example.expert.common.weather.dto.WeatherDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherClient {

  private final RestTemplate restTemplate;

  public WeatherClient(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  public String getTodayWeather() {
    ResponseEntity<WeatherDto[]> responseEntity = restTemplate.getForEntity(
        buildWeatherApiUri(),
        WeatherDto[].class
    );

    WeatherDto[] weatherArray = responseEntity.getBody();

    boolean isErrorStatus = !HttpStatus.OK.equals(
        responseEntity.getStatusCode()
    );

    if (isErrorStatus) {
      throw new ServerException(
          "Weather data is failing to retrieve. Status code: "
              + responseEntity.getStatusCode()
      );
    }
    boolean isWeatherDataInvalid =
        weatherArray == null || weatherArray.length == 0;

    if (isWeatherDataInvalid) {
      throw new ServerException("Weather data is not found");
    }

    String today = getCurrentDate();

    for (WeatherDto weatherDto : weatherArray) {
      if (today.equals(weatherDto.date())) {
        return weatherDto.weather();
      }
    } // todo stream으로?

    throw new ServerException("Weather data for today is not found");
  }

  private URI buildWeatherApiUri() {
    return UriComponentsBuilder
        .fromUriString("https://f-api.github.io")
        .path("/f-api/weather.json")
        .encode()
        .build()
        .toUri();
  }

  private String getCurrentDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
    return LocalDate.now().format(formatter);
  }
}