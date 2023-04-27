package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.OperationHeaderUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.OperationHeader;
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
@RequestMapping(path = "/operationHeader")
@Api(tags = "API REST Operation")
public class OperationHeaderController {

    private final OperationHeaderUseCase operationHeaderUseCase;

    @GetMapping(value = "/generalRequestId/{generalRequestId}")
    @ApiOperation(value = "Retorna una lista paginada de PEPs")
    ResponseEntity getAllByGeneralRequestId(@PathVariable long generalRequestId) {
        try {
            OperationHeader operationHeader = operationHeaderUseCase.getByGeneralRequestId(generalRequestId);
            return ok(operationHeader);
        } catch (Exception e) {
            log.error("Ocurrio un error al realizar la búsqueda de la operación con el número de solicitud: [{}], excepción: [{}]", generalRequestId, e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
