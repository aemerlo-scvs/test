package com.scfg.core.adapter.persistence.answerQuestionnaireRequest;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "AnswerQuestionnaireRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class AnswerQuestionnaireRequestJpaEntity extends BaseJpaEntity {

    @Column(name = "affirmativeAnswer")
    private Integer affirmativeAnswer;

    @Column(name = "answer", length = 500)
    private String answer;

    @Column(name = "generalRequestId")
    private Long generalRequestId;

    @Column(name = "questionnaireId")
    private Long questionnaireId;

    @Column(name = "questionId")
    private Long questionId;

}
