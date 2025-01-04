package org.example.expert.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final AdminInterceptor adminInterceptor;

  // ArgumentResolver 등록
  @Override
  public void addArgumentResolvers(
      List<HandlerMethodArgumentResolver> resolvers
  ) {
    resolvers.add(new AuthUserArgumentResolver());
  }

  // Interceptor 등록
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(adminInterceptor)
        .addPathPatterns("/admin/**");
  }
}