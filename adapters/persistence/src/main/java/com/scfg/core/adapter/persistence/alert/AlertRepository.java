package com.scfg.core.adapter.persistence.alert;

import com.scfg.core.adapter.persistence.alert.AlertJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends CrudRepository<AlertJpaEntity, Integer> {
  //AlertJpaEntity findByAlert_idAndEnvironment_id(int alert_id,int environment);
}
