package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.RoleMenuDTO;

public interface RoleMenuPort {
    Boolean save(RoleMenuDTO roleMenuDTO);
}
