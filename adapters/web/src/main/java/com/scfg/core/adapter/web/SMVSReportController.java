package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.service.SMVSReportService;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.*;
import com.scfg.core.domain.smvs.ParametersFromDTO;
import com.scfg.core.domain.smvs.TempCajerosDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = SMVSEndpoint.BASE_REPORT)
@Api(tags = "API REST Reportes de Sepelio Masivo")
public class SMVSReportController implements SMVSEndpoint {
    private final SMVSReportService smvsReportService;

    @PostMapping(value = BASE_REPORT_ACTIVE_LIST)
    @ApiOperation(value = "Obtiene el Listado de la Solicitudes y polizas (vigentes, pendientes de activar)")
    ResponseEntity getAllSMVSRequestByPage(@RequestBody ParametersFromDTO filters) {
        try {
            PageableDTO list = smvsReportService.getAllSMVSRequestByPage(filters);
            return ok(list);
        } catch (Exception e) {
            log.error("Ocurrió un error: [{}], al obtener la lista de solicitudes para el reporte de suscripción de Sepelio: [{}]", e.getMessage(), filters.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = BASE_REPORT_ACTIVE_FILE)
    @ApiOperation(value = "Obtiene el Listado de la Solicitudes y polizas (vigentes, pendientes de activar)")
    ResponseEntity getlistSMSReportFile(@RequestBody ParametersFromDTO parametersFromDTO) {
        try {
            FileDocumentDTO fileDocumentDTO = smvsReportService.getReportSMVSFileExcel(parametersFromDTO.getStartDate(), parametersFromDTO.getToDate(), parametersFromDTO.getStatusRequest());
            return ok(fileDocumentDTO);
        } catch (Exception e) {
            log.error("Ocurrió un error al verificar el código de activación: [{}]", "reporte", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = BASE_REPORT_COMMERCIALS)
    @ApiOperation(value = "Obtiene el archivos Excel Reporte comercial")
    ResponseEntity getlistSMSReportCommercials(@RequestBody ReportRequestPolicyDTO reportRequestPolicyDTO) {
        try {

            FileDocumentDTOInf fileDocumentDTO = smvsReportService.ReportSUMSReportCommercialsNew(reportRequestPolicyDTO);
            //return ok(response);
            return ok(fileDocumentDTO);
        } catch (Exception e) {
            log.error("Ocurrió un error al generar el Reporte Comercial", "Reporte Comercial", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = BASE_REPORT_LOAD_CASHIERS)
    @ApiOperation(value = "Carga de los cajeros")
    ResponseEntity setCargarCajeros(@RequestBody List<TempCajerosDto> tempCajerosDtoList) {
        try {

            PersistenceResponse fileDocumentDTO = smvsReportService.savecajeros(tempCajerosDtoList);
            //return ok(response);
            return ok(fileDocumentDTO);
        } catch (Exception e) {
            log.error("Ocurrió un error al generar el Reporte Comercial", "Reporte Comercial", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = LIST_CASHIERS_DETAILS)
    @ApiOperation(value = "Listado detallado de la carga de cajeros")
    ResponseEntity findDetailsLoadCashiers() {
        try {
            List<DetailsLoadCashiers1> list = smvsReportService.getDetailLoadCashiers();
            if (list!=null && list.size()>0){
                list=list.stream().sorted(Comparator.comparing(DetailsLoadCashiers1::getCourtDate).reversed()).collect(Collectors.toList());
            }

            //return ok(response);
            return ok(list);
        } catch (Exception e) {
            log.error("Ocurrió un error al generar el Reporte Comercial", "Reporte Comercial", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = FORMAT_FILE_LOAD_SALES)
    @ApiOperation(value = "Formato para la carga de los caejeros")
    ResponseEntity formatFileLoadSales() {
        try {

            FileDocumentDTO fileDocumentDTO = smvsReportService.getFormatLoadFileSales();
            return ok(fileDocumentDTO);
        } catch (Exception e) {
            log.error("Error al descargar formato", "Reporte Comercial", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
