package com.scfg.core.adapter.persistence.document;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentJpaEntity,Long> {

}
