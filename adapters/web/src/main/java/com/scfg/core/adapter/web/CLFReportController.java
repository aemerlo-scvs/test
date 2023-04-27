package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CLFReportUseCase;
import com.scfg.core.domain.dto.credicasas.ClfProcessRequestDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.SearchReportParamDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = CLFReportEndPoint.BASE)
@Api(value = "REST API Generación de reportes para Lafuente")
public class CLFReportController {

    private final CLFReportUseCase clfReportUseCase;

    @PostMapping(value = CLFReportEndPoint.REPORT_CLF)
    @ApiOperation(value = "Generación del reporte CLF")
    ResponseEntity getReportCLF(@RequestBody SearchReportParamDTO search){
        try {
            ClfProcessRequestDTO response = clfReportUseCase.getReportData(search);
            return ok(response);
        } catch (Exception e) {
            log.error("Ocurrio un error al querer generar el reporte: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = CLFReportEndPoint.REPORT_GEL_COMERCIAL)
    @ApiOperation(value = "Generación del reporte comercial - GEL")
    ResponseEntity getComercialReportGEL(@RequestBody SearchReportParamDTO search){
        try {
            ClfProcessRequestDTO response = clfReportUseCase.getComercialReportData(search);
            return ok(response);
        } catch (Exception e) {
            log.error("Ocurrio un error al querer generar el reporte: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
