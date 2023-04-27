package com.scfg.core.adapter.persistence.classifierType;

import com.scfg.core.domain.common.ClassifierType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ClassifierTypeRepository extends CrudRepository<ClassifierTypeJpaEntity, Long> {

}
