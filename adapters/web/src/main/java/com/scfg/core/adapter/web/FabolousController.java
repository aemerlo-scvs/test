package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.FabolousUseCase;
import com.scfg.core.common.util.MyProperties;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.FabolousDTO;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.fabolous.FabolousReportDTO;
import com.scfg.core.domain.dto.fabolous.FabolousSearchCltDTO;
import com.scfg.core.domain.dto.fabolous.FabolousUploadDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = FabolousEndPoint.FABOLOUS_BASE_ROUTE)
@Api(value = "API Poliza Fabulosa")
public class FabolousController implements FabolousEndPoint{

    @Autowired
    MyProperties path;
    private final FabolousUseCase fabolousUseCase;

    @PostMapping
    @ApiOperation(value = "Registra nuevos registros en cuenta Fabulosa")
    public ResponseEntity<PersistenceResponse> saveReport(@RequestBody List<FabolousDTO> fabolousDTOS){
        try {
            PersistenceResponse response = fabolousUseCase.registerReport(fabolousDTOS);
            return ok(response);
        } catch (Exception e){
            log.error("Ocurrió un error al querer registrar el reporte: [{}]", fabolousDTOS, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = FabolousEndPoint.GET_ALL_UPLOAD)
    @ApiOperation(value = "Lista de todas las cargas de reporte fabulosa")
    @Override
    public ResponseEntity getAllUploads() {
        try {
            List<FabolousUploadDTO> uploads = fabolousUseCase.getAllUploads();
            if (uploads.isEmpty()){
                return CustomErrorType.notContent("No content", "No data");
            } else {
                return ok(uploads);
            }
        } catch (Exception e){
            log.error("Ocurrió un error al querer listar los reportes subidos: [{}]", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = {FabolousEndPoint.LIQUIDATION_DONWLOAD_REPORT},
    produces = {"application/json"})
    public ResponseEntity generateReport(@RequestBody FabolousReportDTO report){
        try {
            FileDocumentDTO file = fabolousUseCase.liquidationGenerateReport(report);
            if (file == null) {
                log.error("Ocurrió un error al querer generar el reporte: [{}]", "No se pudo generar el reporte");
                return CustomErrorType.serverError("Server Error", "Error al generar el reporte");
            } else {
                return ok(file);
            }
        } catch (Exception e) {
            log.error("Ocurrió un error al querer generar el reporte: [{}]", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = {FabolousEndPoint.LIQUITION_DOWNLOAD_DUPLICATE_REPORT},
            produces = {"application/json"})
    public ResponseEntity generateDuplicateReport(@RequestBody FabolousReportDTO report){
        try {
            FileDocumentDTO file = fabolousUseCase.liquidationGenerateDuplicateReport(report);
            if (file == null) {
                log.error("Ocurrió un error al querer generar el reporte: [{}]", "No se pudo generar el reporte");
                return CustomErrorType.serverError("Server Error", "Error al generar el reporte");
            } else {
                return ok(file);
            }
        } catch (Exception e) {
            log.error("Ocurrió un error al querer generar el reporte: [{}]", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @DeleteMapping(value = FabolousEndPoint.DELETE_UPLOAD)
    @ApiOperation(value = "Metodo para cambiar el estado de una carga")
    public ResponseEntity deleteUploadReport(@PathVariable Long deleteId){
        try {
            boolean delete = fabolousUseCase.deleteUploadReport(deleteId);
            if (delete) {
                return ok(delete);
            } else {
                log.error("Ocurrió un error al querer eliminar el reportUploadId: ", deleteId);
                return CustomErrorType.serverError("Server Error", "Fallo al querer eliminar el reportUpload con id: " + deleteId);
            }
        } catch (Exception e) {
            log.error("Ocurrió un error al querer eliminar: [{}]", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @Override
    @GetMapping(value = FabolousEndPoint.DONWLOAD_FORMAT)
    @ApiOperation(value = "Metodo para descargar el formato a subir del reporte")
    @ResponseBody
    public ResponseEntity<Resource> downloadFormat() {
        try {
            Path dir = Paths.get(path.getPathFbsExcel());
            Resource resource = new UrlResource(dir.toUri());

            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error al queres descargar el documento de formato", e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = SEARCH_CLIENT)
    @ApiOperation(value = "Metodo para identificar al asegurado en cuenta fabulosa")
    ResponseEntity searchClient(@RequestParam Integer page, @RequestParam Integer size,
                                @RequestBody FabolousSearchCltDTO client) {
        try {
            if (client == null) {
                return CustomErrorType.badRequest("Problema al buscar en el servidor", "No puede mandar ningun campo vacio");
            } else if ((client.getDocumentNumber() == null || client.getDocumentNumber().isEmpty() || client.getDocumentNumber().trim().length() < 1)
                    && (client.getName() == null || client.getName().isEmpty() || client.getName().trim().length() < 1)) {
                return CustomErrorType.badRequest("Problema al buscar en el servidor", "Al menos un campo no debe de estar vacio");
            } else {

                return ok(fabolousUseCase.searchClient(client,page, size));
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }
}
