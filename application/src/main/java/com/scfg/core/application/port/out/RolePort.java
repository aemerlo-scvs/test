package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.Role;

import java.util.List;

public interface RolePort {

    Role findById(long id);

    Role findByName(String name);

    List<Role> findAll();

    Role save(Role role);

    Role update(Role role);

    Role delete(Role role);

    Boolean existName(String name);

}
