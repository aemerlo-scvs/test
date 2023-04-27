package com.scfg.core.security;

import com.scfg.core.common.util.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class SecurityAuditorAware implements AuditorAware<Long> {

    @Autowired
    private RequestInfo requestInfo;

    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if (requestInfo.getUserId() == null) {
            return null;
        }
        Long autUserId = Long.parseLong(requestInfo.getUserId());

        return Optional.of(autUserId);
    }
}
