package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.PersonUseCase;
import com.scfg.core.application.port.in.QuestionnaireUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.dto.credicasas.QuestionnaireDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = QuestionnaireEndPoint.BASE)
@Api(tags = "API REST Cuestionarios")
public class QuestionnaireController {

    private final PersonUseCase personUseCase;
    private final QuestionnaireUseCase questionnaireUseCase;

    @GetMapping(value = QuestionnaireEndPoint.PARAM_QUESTIONNAIRE_CLF)
    @ApiOperation(value = "Retorna un cuestionario por id")
    ResponseEntity getQuestionnaireById(@PathVariable Long questionnaireId) {
        try {
            QuestionnaireDTO questionnaireDTO = questionnaireUseCase.getQuestionnaireById(questionnaireId);
            return ok(questionnaireDTO);
        } catch (NotDataFoundException e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            // log.error("Ocurrio un error al obtener el rol: [{}]", roleId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
