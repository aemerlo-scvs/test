package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.Role;
import com.scfg.core.domain.common.RoleMenuDTO;

import java.util.List;

public interface RoleUseCase {
    Role getById(long id);

    List<Role> get();

    Role save(Role role);

    Role update(Role role);

    Role delete(long roleId);

     Boolean existName(String name);
}
