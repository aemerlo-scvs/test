package com.scfg.core.application.service;

import com.scfg.core.application.port.in.QuestionnaireUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.domain.dto.credicasas.QuestionDTO;
import com.scfg.core.domain.dto.credicasas.QuestionnaireDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionnaireService implements QuestionnaireUseCase {

    private final QuestionnairePort questionnairePort;
    private final QuestionPort questionPort;
    private final QuestionnaireHasQuestionsPort questionnaireHasQuestionsPort;
    private final AnswerQuestionnaireRequestPort answerQuestionnaireRequestPort;


    @Override
    public QuestionnaireDTO getQuestionnaireById(Long id) {

        QuestionnaireDTO questionnaireDTO = questionnairePort.getQuestionnaireById(id);
        List<QuestionDTO> questionDTOList = questionPort.getQuestionsByQuestionnaireId(id);

        questionnaireDTO.setQuestions(questionDTOList);
        return questionnaireDTO;
    }
}
