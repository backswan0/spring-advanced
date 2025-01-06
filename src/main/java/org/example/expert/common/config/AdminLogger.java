package org.example.expert.common.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminLogger {

  private static final Logger logger = LoggerFactory.getLogger(AdminLogger.class);
  private static final String logFormat = "Admin access: URL = %s, Time: %s";

  // 관리자 접근 로그 기록
  public static void log(String url) {

    // 마이크로초를 제거하여 현재 시간을 LocalDateTime으로 가져옴
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String formattedDate = now.format(formatter);

    // 로그 포맷에 맞춰 URL과 시간을 기록
    logger.info(String.format(logFormat, url, formattedDate));
  }
}