package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.AnswerDTO;

import java.util.List;

public interface AnswerQuestionnaireRequestPort {

    boolean saveAllOrUpdateAnswers(List<AnswerDTO> answers);
}
