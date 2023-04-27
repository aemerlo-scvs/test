package com.scfg.core.adapter.persistence.roleMenu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleMenuRepository extends JpaRepository<RoleMenuJpaEntity, RoleMenuKey> {

    @Modifying
    @Query("DELETE FROM RoleMenuJpaEntity WHERE role.id = :roleId")
    void deleteAllByRoleId(@Param("roleId") long roleId);

}
