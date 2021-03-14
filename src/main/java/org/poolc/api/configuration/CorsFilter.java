package org.poolc.api.configuration;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, HEAD");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Content-Length, Accept-Encoding, Authorization");
        res.setHeader("Access-Control-Max-Age", "3600");

        chain.doFilter(request, response);
    }
}
