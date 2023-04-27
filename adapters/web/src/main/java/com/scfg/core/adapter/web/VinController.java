package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.VinUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.RequestCancelOperationDTO;
import com.scfg.core.domain.dto.RequestDetailOperationDTO;
import com.scfg.core.domain.dto.vin.OperationDetailDTO;
import com.scfg.core.domain.dto.vin.VinReportFilterDTO;
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
@RequestMapping(path = "/vin")
@Api(tags = "API REST Vin")
public class VinController {
    private final VinUseCase vinUseCase;

    @PostMapping(value = "/cancelPolicy")
    @ApiOperation(value = "Servicio que anula la poliza")
    ResponseEntity cancelPolicy(@RequestBody RequestCancelOperationDTO cancelOperationDTO) {
        try {
            this.vinUseCase.cancelPolicy(cancelOperationDTO);
            return ok(true);
        } catch (OperationException e) {
            log.error("Ocurrió un error al cancelar la poliza: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al cancelar la poliza: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }

    @PostMapping(value = "/validateOperationDetail")
    @ApiOperation(value = "Servicio que valida el detalle de la operación")
    ResponseEntity isOperationDetailValid(@RequestBody OperationDetailDTO operationDetailDTO) {
        try {
            this.vinUseCase.isOperationDetailValid(operationDetailDTO);
            return ok(true);
        } catch (OperationException e) {
            log.error("Ocurrió un error al validar el detalle de la operación: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request",    e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al validar el detalle de la operación: [{}]", e.toString());
            return CustomErrorType.serverError("Server error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }

    @PostMapping(value = "/operationDetail")
    @ApiOperation(value = "Obtiene Datos del Detalle de la Operación")
    ResponseEntity operationDetail(@RequestBody RequestDetailOperationDTO detailOperationDTO) {
        try {
            return ok(this.vinUseCase.getOperationDetail(detailOperationDTO));
        } catch (OperationException e) {
            log.error("Ocurrió un error al obtener el detalle de la operación: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request",    e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }

    @PostMapping(value = "/productionReport")
    @ApiOperation(value = "Generación del reporte de Producción VIN")
    ResponseEntity getProductionReport(@RequestBody VinReportFilterDTO filter){
        try {
            FileDocumentDTO response = vinUseCase.generateProductionReport(filter);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrió un error al querer generar el reporte de producción: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al querer generar el reporte de producción: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/commercialReport")
    @ApiOperation(value = "Generación del reporte Comercial VIN")
    ResponseEntity getCommercialReport(@RequestBody VinReportFilterDTO filter){
        try {
            FileDocumentDTO response = vinUseCase.generateCommercialReport(filter);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrió un error al querer generar el reporte comercial: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al querer generar el reporte comercial: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
