package com.scfg.core.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.Serializable;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> implements Serializable {

    private JwtTokenProvider jwtTokenProvider;

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtConfigurer(JwtTokenProvider jwtTokenProvider, SecretKey secretKey, JwtConfig jwtConfig) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider, secretKey, jwtConfig);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
