package com.scfg.core.adapter.persistence.documentTemplate;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplateJpaEntity, Long> {

    @Query(value = "SELECT *FROM DocumentTemplate", nativeQuery = true)
    List<DocumentTemplateJpaEntity> findOptionalDocumentTemplateJpaEntityAll();

    List<DocumentTemplateJpaEntity> findAll();

}
