package com.nive.prjt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author nive
 * @class CorsConfig
 * @desc Security 도입 전 전역 Cors 설정 config
 * @since 2025-01-15
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 엔드포인트에 대해 적용
//                        .allowedOrigins(
//                                "http://localhost",       // localhost
//                                "http://127.0.0.1",      // localhost (IP 형식)
//                                "http://192.168.0.21",    // 내부망 IP
//                                "http://your-external-ip" // 외부망 IP
//                        )
                        .allowedOriginPatterns(
                                "http://localhost",       // localhost
                                "http://127.0.0.1",      // localhost (IP 형식)
                                "http://192.168.*.*",    // 내부망 전체
                                "http://외부망IP" // 외부망 IP
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(true) // 인증 정보 포함 허용
                        .maxAge(3600); // 옵션 요청 캐시 시간 (초 단위)
            }
        };
    }
}
