package com.scfg.core.adapter.persistence.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface QuestionRepository extends JpaRepository<QuestionJpaEntity, Long> {

    @Query("SELECT q " +
            "FROM QuestionJpaEntity q " +
            "JOIN QuestionnaireHasQuestionsJpaEntity qhq " +
            "ON qhq.questionId = q.id " +
            "WHERE qhq.questionnaireId = :questionnaireId AND qhq.status = :status")
    List<QuestionJpaEntity> getAllByQuestionnaireId(@Param("questionnaireId") long questionnaireId, @Param("status") Integer status);
}
