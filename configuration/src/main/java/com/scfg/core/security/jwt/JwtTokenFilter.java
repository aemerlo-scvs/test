package com.scfg.core.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Slf4j
public class JwtTokenFilter extends GenericFilterBean implements Serializable {

    private JwtTokenProvider jwtTokenProvider;

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    JwtTokenFilter(JwtTokenProvider jwtTokenProvider, SecretKey secretKey, JwtConfig jwtConfig) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) { //Si entra aqui la petición sera rechazada
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try{
            Authentication auth = jwtTokenProvider.getAuthentication(token);
                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
        }catch (JwtException e){
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

}
