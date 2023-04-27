package com.scfg.core.domain.dto.credicasas.groupthefont.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class AnswerDTO {
    private Long questionId;
    private Long questionnaireId;
    private Boolean affirmativeAnswer;
    private Long parentQuestionId;
    private String answer;
    private Integer questionOrder;
    private Long generalRequestId;
    private String questionDescription;
}
