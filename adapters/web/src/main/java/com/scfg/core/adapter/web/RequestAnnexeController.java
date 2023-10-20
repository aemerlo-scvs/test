package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.RequestAnnexeUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.RequestAnnexe;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.RequestAnnexeCancelaltionDto;
import com.scfg.core.domain.dto.RequestAnnexeSearchFiltersDto;

import com.scfg.core.domain.dto.RequestSaveVoucherPaymentDto;
import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;
import com.scfg.core.domain.common.AnnexeRequirementControl;
import com.scfg.core.domain.dto.vin.RequestAnnexeFileDocumentDTO;
import com.scfg.core.domain.dto.vin.UpdateRequestAnnexeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;


import java.util.List;
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
            List<AnnexeRequirementControl> requirementList = requestAnnexeUseCase.processRequest(requestAnnexeDTO);
            return ok(requirementList);
        } catch (OperationException e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (NoSuchElementException e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PutMapping(value = "/processRequest")
    @ApiOperation(value = "Se valida todo el proceso y se genera los documentos según sean necesarios")
    ResponseEntity updateRequest(@RequestBody UpdateRequestAnnexeDTO requestAnnexeDTO) {
        try {
            List<AnnexeRequirementDto> requirementList = requestAnnexeUseCase.processRequest(requestAnnexeDTO);
            return ok(requirementList);
        } catch (OperationException e) {
            log.error("Ocurrió un error al editar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (NoSuchElementException e) {
            log.error("Ocurrió un error al editar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al editar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/hasPendingRequests/{annexeTypeId}/{policyId}")
    @ApiOperation(value = "Retorna una solictud existente por el Id del tipo de anexo")
    ResponseEntity hasPendingRequests(@PathVariable long annexeTypeId, @PathVariable long policyId ) {
        try {
            RequestAnnexeFileDocumentDTO response = requestAnnexeUseCase.hasPendingRequests(policyId,annexeTypeId);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/search-annexe-request")
    @ApiOperation(value = "Retorna una lista de anexos por filtros dinamicos")
    ResponseEntity getAllByFilters(@RequestParam Integer page, @RequestParam Integer size,
                                   @RequestBody @Nullable RequestAnnexeSearchFiltersDto requestAnnexeSearchFiltersDto) {
        try {
            PageableDTO response = requestAnnexeUseCase.getAllPageByAnnexeFilters(page, size, requestAnnexeSearchFiltersDto);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrio un error al obtener la lista de anexos: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la lista de anexos: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/process-cancellation-annexe-request/{requestAnnexeId}")
    @ApiOperation(value = "Procesa la anulacion de anulacion de una solicitud y genera un anexo")
    ResponseEntity processCancellationRequestAnnexe(@PathVariable Long requestAnnexeId,
                                                    @RequestBody RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto) {
        try {
            this.requestAnnexeUseCase.processCancellationRequest(requestAnnexeId ,requestAnnexeCancelaltionDto);
            return ok(true);
        } catch (OperationException e) {
            log.error("Ocurrio un error al procesar la anulacion de la solicitud [{}] : [{}]", requestAnnexeId, e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al procesar la anulacion de la solicitud [{}] : [{}]",requestAnnexeId, e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/save-voucher-payment/{annexeId}")
    @ApiOperation(value = "Guarda el voucher de pago")
    ResponseEntity processVoucherPayment(@PathVariable Long annexeId,
                                         @RequestBody RequestSaveVoucherPaymentDto requestSaveVoucherPaymentDto) {
        try {
            this.requestAnnexeUseCase.saveVoucherPayment(annexeId, requestSaveVoucherPaymentDto);
            return ok(true);
        } catch (OperationException | NotDataFoundException e) {
            log.error("Ocurrio un error al guardar el vousher de pago [{}] : [{}]", annexeId, e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al guardar el vousher de pago [{}] : [{}]",annexeId, e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/validate-voucher-payment/{requestAnnexeId}")
    @ApiOperation(value = "valida si se podra guardar el voucher de pago")
    ResponseEntity validateVoucherPayment(@PathVariable Long requestAnnexeId){
        try {
            return ok(this.requestAnnexeUseCase.validateVousherPayment(requestAnnexeId));
        } catch (OperationException | NotDataFoundException e) {
            log.error("Ocurrio un error al validar el guardado del voucher de pago [{}] : [{}]", requestAnnexeId, e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al validar el guardado del voucher de pago [{}] : [{}]",requestAnnexeId, e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/update-requirements/{requestAnnexeId}")
    @ApiOperation(value = "Actualiza los documentos de los requerimientos de una solicitud de anexo por el id")
    ResponseEntity updateRequirements(@PathVariable Long requestAnnexeId,
                                      @RequestBody RequestAnnexeCancelaltionDto request) {
        try {
            this.requestAnnexeUseCase.updateRequirements(request, requestAnnexeId);
            return ok(true);
        } catch (OperationException | NotDataFoundException e) {
            log.error("Ocurrio un error al actualizar los requerimeintos de la soliitud [{}] : [{}]", requestAnnexeId, e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al actualizar los requerimeintos de la soliitud [{}] : [{}]",requestAnnexeId, e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
