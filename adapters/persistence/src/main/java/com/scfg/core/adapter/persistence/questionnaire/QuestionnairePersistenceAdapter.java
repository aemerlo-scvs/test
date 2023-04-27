package com.scfg.core.adapter.persistence.questionnaire;

import com.scfg.core.application.port.out.QuestionnairePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.dto.credicasas.QuestionnaireDTO;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class QuestionnairePersistenceAdapter implements QuestionnairePort {

    private final QuestionnaireRepository questionnaireRepository;

    @Override
    public QuestionnaireDTO getQuestionnaireById(Long id) {
        QuestionnaireJpaEntity questionnaireJpaEntity = questionnaireRepository.customFindById(id, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return (questionnaireJpaEntity != null) ? mapToDomain(questionnaireJpaEntity) : null;
    }

    //#region Mappers

    private QuestionnaireDTO mapToDomain(QuestionnaireJpaEntity questionnaireJpaEntity) {
        QuestionnaireDTO questionnaire = QuestionnaireDTO.builder()
                .id(questionnaireJpaEntity.getId())
                .name(questionnaireJpaEntity.getName())
                .typeIdc(questionnaireJpaEntity.getTypeIdc())
                .createdAt(questionnaireJpaEntity.getCreatedAt())
                .lastModifiedAt(questionnaireJpaEntity.getLastModifiedAt())
                .build();

        return questionnaire;
    }

    //#endregion
}
