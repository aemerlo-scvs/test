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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @PostMapping(value = "/vinRecertificate")
    @ApiOperation(value = "Regenerador de certificado - VIN")
    ResponseEntity regenerateCertificateVin( Long planId, Integer creditTermInYears, Date date) {
        try {
//            List<FileDocument> fileDocument = this.generateCertificateCoverageUseCase.regenerateVINCertificateCoverage(planId, creditTermInYears, date);
            Date today = new Date();
            String dateLimit = "10-10-2023 23:59:59";
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date dateComparative = format.parse(dateLimit);
            if(today.after(dateComparative)) {
                return CustomErrorType.badRequest("Process Error", "El método no se puede utilizar después del " + dateLimit);
            }
            String fileDocument = this.generateCertificateCoverageUseCase.regenerateVINCertificateCoverage(planId, creditTermInYears, date);

            return ok(fileDocument);
        } catch (OperationException ex) {
            log.error("Ocurrió un error al regenerar el certificado: [{}]", ex.getMessage());
            return CustomErrorType.badRequest("Process Error", ex.getMessage());
        } catch (Exception ex) {
            log.error("Ocurrió un error al regenerar el certificado: [{}]", ex.getMessage());
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
}
