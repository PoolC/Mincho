package org.poolc.api.board.infra;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.auth.vo.ParsedTokenValues;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class BoardBearerAuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        try {
            ParsedTokenValues userInfo = getUserInfo(request);
            request.setAttribute("UUID", userInfo.getUUID());
            request.setAttribute("isAdmin", userInfo.getIsAdmin());
            return true;
        } catch (NoSuchElementException | NullPointerException e) {
            if (request.getRequestURI().equals("/board") && request.getMethod().equals("GET"))
                return true;
            throw new UnauthenticatedException("Not user");
        }
    }

    private ParsedTokenValues getUserInfo(HttpServletRequest request) {
        String token = parsingJwtToken(request.getHeader("authorization"));
        Claims body = jwtTokenProvider.getBodyFromJwtToken(token);
        ParsedTokenValues userInfo = new ParsedTokenValues(body);
        return userInfo;
    }

    private String parsingJwtToken(String token) {
        if (token.contains("Bearer"))
            return token.replace("Bearer ", "");

        throw new NoSuchElementException("No token");
    }
}
