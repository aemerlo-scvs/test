package com.scfg.core.security.jwt;


import com.scfg.core.domain.common.AuthUser;
import com.scfg.core.security.AuthUserDetailsService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private AuthUserDetailsService securityUserDetailsService;


    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    public String createToken(String username, AuthUser authUSer) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtConfig.getTokenExpirationAfterMilliseconds());

        String token = Jwts.builder()
                .setSubject(username)
                .claim("authorities", authUSer.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
        return token;
    }

    public String createTokenApi(String username, Date fechaInicio, Date fechaExpiracion) {
        Claims claims = Jwts.claims().setSubject(username);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(fechaInicio)
                .setExpiration(fechaExpiracion)
                .signWith(secretKey)
                .compact();
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = this.securityUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody().getSubject();

        //return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            try {
                return bearerToken.substring(7, bearerToken.length());
            }catch (Exception e) {
                log.error("Usuario no encontrado, UsuarioId[{}]", req.getHeader("username"));
                return null;
            }
        }
        return null;
    }

}
