package org.poolc.api.board.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class BoardWebMvcConfig implements WebMvcConfigurer {
    private final BoardBearerAuthInterceptor boardBearerAuthInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(boardBearerAuthInterceptor).addPathPatterns("/board");
        registry.addInterceptor(boardBearerAuthInterceptor).addPathPatterns("/board/**");
    }
}
