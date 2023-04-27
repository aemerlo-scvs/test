package com.scfg.core.security.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[init], CORS Filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        response.setHeader("Access-Control-Allow-Origin", this.getUrl(request));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getUrl(HttpServletRequest request) throws MalformedURLException {
        String url = "*";

        String requestUrl = request.getHeader("origin");
        String domain = new URL(request.getRequestURL().toString()).getHost();

        List<String> allowedDomains = Arrays.asList("localhost", "10.170.221.74", "10.170.222.75", "10.170.222.70", "svrsepelioprod", "santacruzvidaysalud.com.bo");

        for (String d : allowedDomains) {
            if (domain.equals(d)) {
                url = requestUrl;
                break;
            }
        }

        return url;
    }

    @Override
    public void destroy() {

    }
}
