package com.scfg.core.adapter.persistence.roleMenuAction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleMenuActionRepository extends JpaRepository<RoleMenuActionJpaEntity, RoleMenuActionKey> {

    @Modifying
    @Query("DELETE FROM RoleMenuActionJpaEntity WHERE id.roleId = :roleId")
    void deleteAllByRoleId(@Param("roleId") long roleId);

}
