package com.scfg.core.adapter.persistence.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuJpaEntity, Long> {


//    String queryFindAllByStatusAndRoleId = "SELECT m.* " +
//                                                "FROM Menu m " +
//                                                "INNER JOIN RoleMenu rm ON  rm.menuId = m.id " +
//                                                "WHERE m.status = :status AND rm.roleId = :roleId";
//    @Query( value = queryFindAllByStatusAndRoleId, nativeQuery = true)
//    List<MenuJpaEntity> findAllByStatusAndRoleId(@Param("status") long status, @Param("roleId") long roleId);
//     AND m.parentId IS NULL
    @Query("SELECT m " +
            "FROM MenuJpaEntity m " +
            "INNER JOIN RoleMenuJpaEntity rm ON rm.menu.id = m.id " +
            "WHERE rm.role.id = :roleId")
    List<MenuJpaEntity> findAllByRoleId(@Param("roleId") long roleId);

    @Modifying
    @Query("UPDATE MenuJpaEntity " +
            "SET status = :status " +
            "WHERE id = :id OR parentId = :id")
    void deleteAllByIdOrParentId(@Param("status") int status, @Param("id") long id);
//
//    Optional<MenuJpaEntity> findById(long id);
//
//    Optional<List<MenuJpaEntity>> findAllByIdOrParentId(long id, long parentId);

    List<MenuJpaEntity> findAllByParentIdIsNull();
    @Query("select case when count(c)> 0 then true else false end from MenuJpaEntity c where c.status=1 and c.parentId is null and lower(c.name) = lower(:name)")
    Boolean exitNameFather(@Param("name")String name);

}
