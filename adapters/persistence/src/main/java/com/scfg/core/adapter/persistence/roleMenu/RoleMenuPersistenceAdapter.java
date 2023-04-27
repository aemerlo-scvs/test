package com.scfg.core.adapter.persistence.roleMenu;

import com.scfg.core.adapter.persistence.roleMenuAction.RoleMenuActionJpaEntity;
import com.scfg.core.adapter.persistence.roleMenuAction.RoleMenuActionKey;
import com.scfg.core.adapter.persistence.roleMenuAction.RoleMenuActionRepository;
import com.scfg.core.application.port.out.RoleMenuPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.RoleMenuDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
class RoleMenuPersistenceAdapter implements RoleMenuPort {

    private final RoleMenuRepository roleMenuRepository;
    private final RoleMenuActionRepository roleMenuActionRepository;

    @Override
    public Boolean save(RoleMenuDTO roleMenuDTO) {

        Long roleId = roleMenuDTO.getRole().getId();

        roleMenuActionRepository.deleteAllByRoleId(roleId);
        roleMenuRepository.deleteAllByRoleId(roleId);

        List<RoleMenuJpaEntity> roleMenuJpaEntityList = new ArrayList<>();
        List<RoleMenuActionJpaEntity> roleMenuActionJpaEntityList = new ArrayList<>();

        roleMenuDTO.getMenus().forEach(menu -> {
            RoleMenuJpaEntity roleMenuJpaEntity = new RoleMenuJpaEntity();
            RoleMenuKey roleMenuKey = new RoleMenuKey();
            roleMenuKey.roleId = roleId;
            roleMenuKey.menuId = menu.getId();
            roleMenuJpaEntity.setId(roleMenuKey);
            roleMenuJpaEntityList.add(roleMenuJpaEntity);

            if (menu.getActions() != null) {
                menu.getActions().forEach(action -> {
                    RoleMenuActionJpaEntity roleMenuActionJpaEntity = new RoleMenuActionJpaEntity();
                    RoleMenuActionKey roleMenuActionKey = new RoleMenuActionKey();
                    roleMenuActionKey.roleId = roleId;
                    roleMenuActionKey.menuId = menu.getId();
                    roleMenuActionKey.actionId = action.getId();
                    roleMenuActionJpaEntity.setId(roleMenuActionKey);
                    roleMenuActionJpaEntityList.add(roleMenuActionJpaEntity);
                });
            }
        });

        roleMenuRepository.saveAll(roleMenuJpaEntityList);
        roleMenuActionRepository.saveAll(roleMenuActionJpaEntityList);
        return true;
    }
}
