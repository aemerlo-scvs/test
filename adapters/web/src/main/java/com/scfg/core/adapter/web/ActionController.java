package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ActionUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.common.Action;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = ActionEndpoint.BASE)
@Api(value = "API REST Acciones")
public class ActionController implements ActionEndpoint{

    private final ActionUseCase actionUseCase;

    @GetMapping
    @ApiOperation(value = "Retorna una lista de acciones")
    ResponseEntity getAll() {
        Object actions = actionUseCase.getAll();
        return ok(actions);
    }

    @GetMapping(value = PARAM_ID)
    @ApiOperation(value = "Retorna una acción por el Id")
    ResponseEntity getById(@PathVariable long actionId) {
        try {
            Action action = actionUseCase.getById(actionId);
            return ok(action);
        } catch (NotDataFoundException e) {
            log.error("Ocurrio un error al obtener la acción: [{}]", actionId, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la acción: [{}]", actionId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
