package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.Menu;

import java.util.List;

public interface MenuPort {

    List<Menu> findAll();

    List<Menu> findAllWithDetail();

    Menu findById(long id);

    List<Menu> findAllByRoleId(long roleId);

    Boolean save(Menu menu);

    Menu update(Menu menu);

    Boolean delete(Menu menu);

    Boolean existNameFather(String name);
}
