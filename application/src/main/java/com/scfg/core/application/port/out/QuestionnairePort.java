package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.credicasas.QuestionnaireDTO;

public interface QuestionnairePort {

    QuestionnaireDTO getQuestionnaireById(Long id);
}
