package com.scfg.core.application.port.in;

import com.scfg.core.domain.common.Menu;
import com.scfg.core.domain.common.RoleMenuDTO;

import java.util.List;

public interface MenuUseCase {

    List<Menu> getAll();

    List<Menu> getAllWithDetail();

    Menu getById(long id);

    List<Menu> getByRoleId(long roleId);

    Boolean save(Menu role);

    Boolean update(Menu role);

    Boolean delete(long menuId);
    Boolean existNameFather(String name);
}
