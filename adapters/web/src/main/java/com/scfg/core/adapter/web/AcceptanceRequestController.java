package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.AcceptanceRequestUseCase;
import com.scfg.core.common.enums.ProductEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.MessageDecideResponseDTO;
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
@RequestMapping(path = "/proposalResponse")
@Api(value = "API REST Aceptación de solicitudes")
public class AcceptanceRequestController {

    private final AcceptanceRequestUseCase acceptanceRequestUseCase;

    @PostMapping
    @ApiOperation(value = "Aceptación de propuesta por requestId")
    ResponseEntity proposalResponse(@RequestBody MessageDecideResponseDTO messageResponseDTO){
        try {
            Boolean res = acceptanceRequestUseCase.acceptanceRequest(messageResponseDTO);
            return ok(res);
        }
        catch (OperationException e) {
            log.error("Ocurrio un error al querer aceptar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        }
        catch (Exception e) {
            log.error("Ocurrio un error al querer aceptar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/proposalViewDetail")
    @ApiOperation(value = "Obtiene Datos del la propuesta")
    ResponseEntity operationDetail(@RequestParam Long requestId) {
        try {
            return ok(this.acceptanceRequestUseCase.getProposalDetail(requestId));
        } catch (OperationException e) {
            log.error("Ocurrio un error al obtener el detalle de la propuesta: [{}]", e.toString());
            return CustomErrorType.badRequest("Solicitud no Realizada",    e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener el detalle de la propuesta: [{}]", e.toString());
            return CustomErrorType.serverError("Solicitud no Realizada", "No se pudo realizar la operacion, " + e.getMessage());
        }
    }
}
