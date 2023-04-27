package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.AuthUser;
import com.scfg.core.domain.common.AuthenticationRequest;
import com.scfg.core.domain.common.BfsAuthenticationRequest;

public interface AuthUserUseCase {

      AuthUser auth(AuthenticationRequest data);

      AuthUser automaticLdapAuth(BfsAuthenticationRequest data);

      String generateToken(AuthenticationRequest data, AuthUser authUser);

      AuthUser verifyToken(String token);

}
