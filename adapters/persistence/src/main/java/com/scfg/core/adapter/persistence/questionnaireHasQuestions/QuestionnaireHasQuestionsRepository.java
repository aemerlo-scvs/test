package com.scfg.core.adapter.persistence.questionnaireHasQuestions;

import com.scfg.core.adapter.persistence.questionnaire.QuestionnaireJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionnaireHasQuestionsRepository extends JpaRepository<QuestionnaireHasQuestionsJpaEntity, Long> {

}
