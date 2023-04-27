package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CLFProcessRequestUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.credicasas.ClfProcessRequestDTO;
import com.scfg.core.domain.dto.credicasas.ProcessExistRequestDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.RequestFontDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = CLFProcessRequestEndPoint.BASE)
@Api(value = "Procesos Credicasas La Fuente")
public class CLFProcessRequestController {

    private final CLFProcessRequestUseCase clfProcessRequestUseCase;

    @PostMapping(value = CLFProcessRequestEndPoint.REQUEST)
    @ApiOperation(value = "Se valida todo el proceso y se genera los documentos según sean necesarios")
    ResponseEntity processRequest(@RequestBody RequestFontDTO requestFontDTO){
        try {
            ClfProcessRequestDTO response = clfProcessRequestUseCase.processRequest(requestFontDTO);
                return ok(response);
        }
        catch (OperationException e) {
            log.error("Ocurrio un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        }
        catch (Exception e) {
            log.error("Ocurrio un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = CLFProcessRequestEndPoint.SUSCRIPTIONSAVE)
    @ApiOperation(value = "Validar y cambiar estados desde backoffice a solicitudes creadas")
    ResponseEntity processSubscriptionRequest(@RequestBody ProcessExistRequestDTO processExistRequestDTO){
        try {
            ClfProcessRequestDTO response = clfProcessRequestUseCase.processSubscriptionRequest(processExistRequestDTO);
            return ok(response);
        }
        catch (OperationException e) {
            log.error("Ocurrio un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        }
        catch (Exception e) {
            log.error("Ocurrio un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
