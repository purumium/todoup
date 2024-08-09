package com.kosa.todoup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8000") // Vue.js 개발 서버의 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

/*
- addMapping("/**")
  : CORS를 적용할 URL패턴을 정의
- allowedOrigins("http://localhost:8000")
  : 자원 공유를 허락할 Origin을 지정
- allowedMethods("GET", "POST", "PUT", "DELETE")
  : 허용할 HTTP method를 지정
- allowedHeaders("*")
  : 클라이언트 측의 CORS 요청에 허용되는 헤더를 지정
*/