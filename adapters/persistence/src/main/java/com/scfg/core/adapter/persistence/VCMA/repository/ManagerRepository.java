package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.adapter.persistence.VCMA.models.ManagerJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<ManagerJpaEntity, Integer> {
    //    Manager findByMANAGER_ID(BigInteger id);
//    Manager findByNAMES(String name);
    List<ManagerJpaEntity> findAll();

    @Query(value = "SELECT mng " +
            "FROM ManagerJpaEntity mng " +
            "WHERE LOWER(mng.NAMES) LIKE LOWER(CONCAT('%', :fullnames,'%')) ",
            countQuery = "SELECT COUNT(mng.MANAGER_ID) " +
                    "FROM ManagerJpaEntity mng " +
                    "WHERE LOWER(mng.NAMES) LIKE LOWER(CONCAT('%', :fullnames,'%')) ")
    Page<ManagerJpaEntity> findByNAMES(@Param("fullnames") String fullnames,
                                       Pageable pageable);

    @Query("SELECT COALESCE(MAX(mng.MANAGER_ID),0) " +
            "FROM ManagerJpaEntity mng ")
    Long findMaxManagerId();
}
