package com.scfg.core.adapter.persistence.direction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectionRepository extends JpaRepository<DirectionJpaEntity, Long> {

    List<DirectionJpaEntity> findAllByPersonId(long personId);
    List<DirectionJpaEntity> findAllByNewPersonId(long newPersonId);

    List<DirectionJpaEntity> findAllByPersonIdIn(List<Long> personIdList);

    @Modifying
    @Query("UPDATE DirectionJpaEntity " +
            "SET status = :status " +
            "WHERE personId = :personId AND directionTypeIdc IN (:personalDirectionType, :workDirectionType)")
    void deleteWorkAndPersonalDirectionByPersonId(@Param("status") int status, @Param("personId") long personId,
                                                  @Param("personalDirectionType") int personalDirectionType, @Param("workDirectionType") int workDirectionType);
}
