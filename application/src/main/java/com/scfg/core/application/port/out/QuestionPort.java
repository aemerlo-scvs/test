package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.credicasas.QuestionDTO;

import java.util.List;

public interface QuestionPort {

    List<QuestionDTO> getQuestionsByQuestionnaireId(Long questionnaireId);

    List<QuestionDTO> getAllQuestion();
}
