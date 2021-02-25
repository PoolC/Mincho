package org.poolc.api.post.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class PostWebMvcConfig implements WebMvcConfigurer {
    private final PostBearerAuthInterceptor postBearerAuthInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(postBearerAuthInterceptor).addPathPatterns("/post");
        registry.addInterceptor(postBearerAuthInterceptor).addPathPatterns("/post/**");
    }
}
