package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.GenerateCertificateCoverageUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.FileDocument;
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
@RequestMapping(value = "/generateCertificateCoverage")
@Api(tags = "API REST Generador de certificados")
public class GenerateCertificateCoverageController {

    private final GenerateCertificateCoverageUseCase generateCertificateCoverageUseCase;
    @PostMapping(value = "/vinCertificate")
    @ApiOperation(value = "Generador de certificado - VIN")
    ResponseEntity generateCertificateVin(@RequestParam Long requestId, @RequestBody Object object) {
        try {
            FileDocument fileDocument = this.generateCertificateCoverageUseCase.generateVINCertificateCoverage(requestId, object);
            return ok(fileDocument);
        } catch (OperationException ex) {
            log.error("Ocurrió un error al generar el certificado: [{}]", ex.getMessage());
            return CustomErrorType.badRequest("Process Error", ex.getMessage());
        } catch (Exception ex) {
            log.error("Ocurrió un error al generar el certificado: [{}]", ex.getMessage());
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
}
