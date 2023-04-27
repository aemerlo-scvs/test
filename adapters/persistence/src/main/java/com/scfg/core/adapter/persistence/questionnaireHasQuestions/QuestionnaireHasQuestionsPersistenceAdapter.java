package com.scfg.core.adapter.persistence.questionnaireHasQuestions;

import com.scfg.core.adapter.persistence.plan.PlanJpaEntity;
import com.scfg.core.application.port.out.QuestionnaireHasQuestionsPort;
import com.scfg.core.application.port.out.QuestionnairePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.Plan;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class QuestionnaireHasQuestionsPersistenceAdapter implements QuestionnaireHasQuestionsPort {

    //#region Mappers

    private Plan mapToDomain(PlanJpaEntity planJpaEntity) {
        Plan plan = Plan.builder()
                .id(planJpaEntity.getId())
                .name(planJpaEntity.getName())
                .description(planJpaEntity.getDescription())
                .totalPremium(planJpaEntity.getTotalPremium())
                .rate(planJpaEntity.getRate())
                .totalInsuredCapital(planJpaEntity.getTotalInsuredCapital())
                .applyDiscount(planJpaEntity.getApplyDiscount())
                .creditPremiumSurcharge(planJpaEntity.getCreditPremiumSurcharge())
                .currencyTypeIdc(planJpaEntity.getCurrencyTypeIdc())
                .createdAt(planJpaEntity.getCreatedAt())
                .lastModifiedAt(planJpaEntity.getLastModifiedAt())
                .build();

        return plan;
    }

    //#endregion
}
