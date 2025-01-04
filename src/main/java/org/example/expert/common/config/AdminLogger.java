package org.example.expert.common.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminLogger {

  private static final Logger logger = LoggerFactory.getLogger(AdminLogger.class);
  private static final String logFormat = "Admin access: URL = %s, Time: %s";

  public static void log(String url) {
    // 마이크로초 제거
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String formattedDate = now.format(formatter);

    logger.info(String.format(logFormat, url, formattedDate));
  }
}