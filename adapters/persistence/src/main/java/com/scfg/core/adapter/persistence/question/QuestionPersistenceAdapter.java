package com.scfg.core.adapter.persistence.question;

import com.scfg.core.adapter.persistence.plan.PlanJpaEntity;
import com.scfg.core.adapter.persistence.plan.PlanRepository;
import com.scfg.core.application.port.out.PlanPort;
import com.scfg.core.application.port.out.QuestionPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.credicasas.QuestionDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class QuestionPersistenceAdapter implements QuestionPort {

    private final QuestionRepository questionRepository;

    @Override
    public List<QuestionDTO> getQuestionsByQuestionnaireId(Long questionnaireId) {
        List<QuestionJpaEntity> questionJpaEntityList = questionRepository.getAllByQuestionnaireId(questionnaireId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return questionJpaEntityList.stream()
                .map(questionJpaEntity -> mapToDomain(questionJpaEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> getAllQuestion() {
        List<QuestionJpaEntity> questionJpaEntityList = questionRepository.findAll();
        return questionJpaEntityList.stream()
                .map(questionJpaEntity -> mapToDomain(questionJpaEntity))
                .collect(Collectors.toList());
    }

    //#region Mappers

    private QuestionDTO mapToDomain(QuestionJpaEntity questionJpaEntity) {
        QuestionDTO questionDTO = QuestionDTO.builder()
                .id(questionJpaEntity.getId())
                .content(questionJpaEntity.getContent())
                .screenContent(questionJpaEntity.getScreenContent())
                .questionCategoryIdc(questionJpaEntity.getQuestionCategoryIdc())
                .questionTypeIdc(questionJpaEntity.getQuestionTypeIdc())
                .designTypeIdc(questionJpaEntity.getDesignTypeIdc())
                .parentId(questionJpaEntity.getParentId())
                .createdAt(questionJpaEntity.getCreatedAt())
                .lastModifiedAt(questionJpaEntity.getLastModifiedAt())
                .build();

        return questionDTO;
    }

    //#endregion
}
