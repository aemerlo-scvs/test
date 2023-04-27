package com.scfg.core.adapter.persistence.VCMA.repository;

import com.scfg.core.adapter.persistence.VCMA.models.ZoneJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends CrudRepository<ZoneJpaEntity, Integer> {
//    Zone findByZONES_ID(BigInteger id);
//    Zone findByDESCRIPTION(String name);
    List<ZoneJpaEntity> findAll();
}
