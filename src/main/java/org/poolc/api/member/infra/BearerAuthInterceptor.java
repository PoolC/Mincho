package org.poolc.api.member.infra;

import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.auth.vo.ParsedTokenValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public BearerAuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("POST")) {
            return true;
        }
        String token = parsingJwtToken(request.getHeader("authorization"));
        ParsedTokenValues userInfo = jwtTokenProvider.getUserInfo(token);
        request.setAttribute("UUID", userInfo.getUUID());
        request.setAttribute("isAdmin", userInfo.getIsAdmin());
        return true;
    }

    private String parsingJwtToken(String token) {
        if (token.contains("Bearer"))
            return token.replace("Bearer ", "");

        if (token.contains("Basic"))
            return token.replace("Basic ", "");

        return token;
    }
}