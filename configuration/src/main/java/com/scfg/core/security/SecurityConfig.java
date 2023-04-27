package com.scfg.core.security;

import com.scfg.core.security.filters.CorsFilter;
import com.scfg.core.security.jwt.JwtConfig;
import com.scfg.core.security.jwt.JwtConfigurer;
import com.scfg.core.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.crypto.SecretKey;

// This indicates to springboot that is our web security config

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    String[] whiteList = new String[]{
            "/include/**", "/css/**", "/icons/**", "img/**", "/js/**", "/vendor/**", "/swagger-ui/**", "/classifier/classifierType/**", "/notificationWebSocket/**",
            "/product/plans/**", "/smvs/**", "/vin/cancelPolicy/**", "/vin/validateOperationDetail/**", "/proposalResponse/**"
    };


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
         return super.authenticationManagerBean();
    }

    @Bean
    CorsFilter corsFilter() {
        return new CorsFilter();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'; style-src 'self'; form-action 'self'")
                .and()
                .frameOptions().sameOrigin();


         http.addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .formLogin().disable()
                .csrf().disable() //Cross Site Request Forgery, Springboot recomienda deshabilitarlo sino se tienen usuarios de navegador
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(whiteList).permitAll()
                .antMatchers(HttpMethod.POST, "/user/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider, secretKey, jwtConfig));
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/",
                "/webjars/**");
    }
}
