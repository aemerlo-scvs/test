package com.scfg.core.adapter.persistence.alert;

import com.scfg.core.adapter.persistence.alert.AlertJpaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends CrudRepository<AlertJpaEntity, Integer> {
  //AlertJpaEntity findByAlert_idAndEnvironment_id(int alert_id,int environment);

    @Query("SELECT a FROM AlertJpaEntity a " +
            "WHERE a.alert_id in :ids")
    List<AlertJpaEntity> findAllById(@Param("ids") List<Integer> idList);
}
