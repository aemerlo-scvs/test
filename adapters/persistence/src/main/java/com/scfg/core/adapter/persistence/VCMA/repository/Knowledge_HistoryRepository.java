package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.adapter.persistence.VCMA.models.Knowledge_HistoryJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Knowledge_HistoryRepository extends CrudRepository<Knowledge_HistoryJpaEntity,String> {

}
