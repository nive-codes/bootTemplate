package com.nive.prjt.config;

import com.nive.prjt.config.interceptor.logging.ComLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author nive
 * @class InterceptorConfig
 * @desc Interceptor 관리하는 Config (운영때만 로그를 저장하도록 수정한다.)
 * @since 2025-01-31
 */
@Configuration
@Profile("prod")
public class InterceptorConfig implements WebMvcConfigurer {

    private final ComLoggingInterceptor comLoggingInterceptor;

    public InterceptorConfig(ComLoggingInterceptor comLoggingInterceptor) {
        this.comLoggingInterceptor = comLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(comLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/favicon.ico","/css/**","js/**","/upload/**","/images/**");
    }

}
