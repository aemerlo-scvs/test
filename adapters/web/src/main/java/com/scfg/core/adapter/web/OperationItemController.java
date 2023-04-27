package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.OperationHeaderUseCase;
import com.scfg.core.application.port.in.OperationItemUseCase;
import com.scfg.core.domain.common.OperationHeader;
import com.scfg.core.domain.common.OperationItem;
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
@RequestMapping(path = "/operationItem")
@Api(tags = "API REST Detalle de Operation")
public class OperationItemController {

    private final OperationItemUseCase operationItemUseCase;

    @PostMapping()
    @ApiOperation(value = "Retorna una lista paginada de PEPs")
    ResponseEntity save(@RequestBody OperationItem operationItem) {
        try {
            Boolean response = operationItemUseCase.saveOrUpdate(operationItem);
            return ok(response);
        } catch (Exception e) {
            log.error("Ocurrió un error al guardar el registro: [{}], excepción: [{}]", operationItem, e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
