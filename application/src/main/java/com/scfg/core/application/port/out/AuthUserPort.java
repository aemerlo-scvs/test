package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.AuthUser;

public interface AuthUserPort {

    AuthUser findByUserName(String username);

    AuthUser findByEmail(String email);

    AuthUser findByToken(String token);

    AuthUser update(AuthUser authUser);

}
