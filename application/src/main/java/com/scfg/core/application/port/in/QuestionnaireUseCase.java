package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.credicasas.QuestionnaireDTO;

public interface QuestionnaireUseCase {

    QuestionnaireDTO getQuestionnaireById(Long id);
}
