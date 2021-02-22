package org.poolc.api.project.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProjectWebMvcConfig implements WebMvcConfigurer {

    private final ProjectBearerAuthInterceptor bearerAuthInterceptor;

    public ProjectWebMvcConfig(ProjectBearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/project");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/project/**");
    }
}
