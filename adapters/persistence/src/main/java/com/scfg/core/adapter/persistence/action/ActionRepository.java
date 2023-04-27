package com.scfg.core.adapter.persistence.action;

import com.scfg.core.adapter.persistence.changeLog.ChangeLogJpaEntity;
import com.scfg.core.adapter.persistence.role.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActionRepository extends JpaRepository<ActionJpaEntity, Long> {

    Optional<ActionJpaEntity> findById(long id);

//    @Query("SELECT a " +
//            "FROM ActionJpaEntity a " +
//            "INNER JOIN RoleMenuActionJpaEntity rma ON rma.action.id = a.id " +
//            "WHERE rma.roleMenu.role.id = :roleId AND rma.roleMenu.menu.id = :menuId")

    @Query(value = "SELECT a.* " +
                   "FROM [Action] a " +
                   "INNER JOIN RoleMenuAction rma ON rma.actionId = a.id " +
                   "WHERE rma.roleId = :roleId AND menuId = :menuId", nativeQuery = true)
    List<ActionJpaEntity> findAllByRoleIdAndMenuId(@Param("roleId") long roleId, @Param("menuId") long menuId);

}
