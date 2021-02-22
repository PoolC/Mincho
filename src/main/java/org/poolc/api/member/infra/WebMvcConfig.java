package org.poolc.api.member.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BearerAuthInterceptor bearerAuthInterceptor;

    public WebMvcConfig(BearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/member");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/member/**");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/board");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/board/**");

    }
}