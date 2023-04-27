package com.scfg.core.adapter.persistence.questionnaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface QuestionnaireRepository extends JpaRepository<QuestionnaireJpaEntity, Long> {

    @Query("SELECT q " +
            "FROM QuestionnaireJpaEntity q " +
            "WHERE q.id = :id AND q.status = :status")
    QuestionnaireJpaEntity customFindById(@Param("id") long id, @Param("status") Integer status);
}
