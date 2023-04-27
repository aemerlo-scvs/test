package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.RequestAnnexeUseCase;
import com.scfg.core.common.exception.OperationException;

import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "requestAnnexe")
@Api(tags = "API REST Solicitud de anexo")
public class RequestAnnexeController {
    private final RequestAnnexeUseCase requestAnnexeUseCase;

    @PostMapping(value = "/processRequest")
    @ApiOperation(value = "Se valida todo el proceso y se genera los documentos según sean necesarios")
    ResponseEntity processRequest(@RequestBody RequestAnnexeDTO requestAnnexeDTO) {
        try {
            Boolean response = requestAnnexeUseCase.processRequest(requestAnnexeDTO);
            return ok(response);
            // TODO Realizar el control de errores
        } catch (OperationException e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (NoSuchElementException e){
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        }
        catch (Exception e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = "/hasPendingRequests/{annexeTypeId}/{policyId}")
    @ApiOperation(value = "Retorna la lista de requerimientos por el Id del tipo de anexo")
    ResponseEntity hasPendingRequests(@PathVariable long annexeTypeId, @PathVariable long policyId ) {
        try {
            Boolean response = requestAnnexeUseCase.hasPendingRequests(policyId,annexeTypeId);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }







}
