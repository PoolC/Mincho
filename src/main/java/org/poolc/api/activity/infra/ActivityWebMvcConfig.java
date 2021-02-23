package org.poolc.api.activity.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ActivityWebMvcConfig implements WebMvcConfigurer {
    private final ActivityBearerAuthInterceptor bearerAuthInterceptor;

    public ActivityWebMvcConfig(ActivityBearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/activity");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/activity/**");

    }
}