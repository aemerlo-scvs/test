package com.scfg.core.adapter.persistence.answerQuestionnaireRequest;

import com.scfg.core.adapter.persistence.plan.PlanJpaEntity;
import com.scfg.core.application.port.out.AnswerQuestionnaireRequestPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.AnswerDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnswerQuestionnaireRequestPersistenceAdapter implements AnswerQuestionnaireRequestPort {

    private final AnswerQuestionnaireRequestRepository answerQuestionnaireRequestRepository;

    @Override
    public boolean saveAllOrUpdateAnswers(List<AnswerDTO> answers) {
        List<AnswerQuestionnaireRequestJpaEntity> answerQuestionnaireRequestJpaEntities = new ArrayList<>();
        answers.forEach(e -> {
            AnswerQuestionnaireRequestJpaEntity answerQuestionnaireRequestJpaEntity = mapToJpaEntity(e);
            answerQuestionnaireRequestJpaEntities.add(answerQuestionnaireRequestJpaEntity);
        });
        answerQuestionnaireRequestRepository.saveAll(answerQuestionnaireRequestJpaEntities);
        return true;
    }

    //#region Mappers

//    private Plan mapToDomain(PlanJpaEntity planJpaEntity) {
//        Plan plan = Plan.builder()
//                .id(planJpaEntity.getId())
//                .name(planJpaEntity.getName())
//                .description(planJpaEntity.getDescription())
//                .totalPremium(planJpaEntity.getTotalPremium())
//                .rate(planJpaEntity.getRate())
//                .totalInsuredCapital(planJpaEntity.getTotalInsuredCapital())
//                .applyDiscount(planJpaEntity.getApplyDiscount())
//                .creditPremiumSurcharge(planJpaEntity.getCreditPremiumSurcharge())
//                .currencyTypeIdc(planJpaEntity.getCurrencyTypeIdc())
//                .createdAt(planJpaEntity.getCreatedAt())
//                .lastModifiedAt(planJpaEntity.getLastModifiedAt())
//                .build();
//
//        return plan;
//    }

    private AnswerQuestionnaireRequestJpaEntity mapToJpaEntity(AnswerDTO answerDTO){
        AnswerQuestionnaireRequestJpaEntity answerJpa = AnswerQuestionnaireRequestJpaEntity.builder()
                .affirmativeAnswer((answerDTO.getAffirmativeAnswer() == null) ? null : (answerDTO.getAffirmativeAnswer()) ? 1 : 0)
                .answer(answerDTO.getAnswer())
                .generalRequestId(answerDTO.getGeneralRequestId())
                .questionnaireId(answerDTO.getQuestionnaireId())
                .questionId(answerDTO.getQuestionId())
                .build();
        return answerJpa;
    }


    //#endregion
}
