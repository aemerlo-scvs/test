package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.FileDocumentUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.PolicyFileDocument;
import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = FileDocumentEndpoint.BASE)
@Api(tags = "API Documents")
public class FileDocumentController {
    private final FileDocumentUseCase fileDocumentUseCase;

    @GetMapping(value = FileDocumentEndpoint.GET_DOCUMENT)
    @ApiOperation(value = "Retorna los documentos generados por el sistema de la solicitud")
    ResponseEntity getDocumentsByRequestId(@PathVariable Long requestId) {
        try {
            List<FileDocumentByRequestDTO> fileDocumentByRequestDTOList = fileDocumentUseCase.getDocumentsByRequestId(requestId);
            return ok(fileDocumentByRequestDTOList);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

    @GetMapping(value = FileDocumentEndpoint.GET_SIGNED_DOCUMENT)
    @ApiOperation(value = "Retorna los documentos firmados de la solicitud")
    ResponseEntity getSignedDocumentsByRequestId(@PathVariable Long requestId) {
        try {
            List<FileDocumentByRequestDTO> fileDocumentByRequestDTOList = fileDocumentUseCase.getSignedDocumentsByRequestId(requestId);
            return ok(fileDocumentByRequestDTOList);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

    @GetMapping(value = FileDocumentEndpoint.GET_CERTIFICATE_COVERAGE_DOCUMENT)
    @ApiOperation(value = "Retorna certificado de cobertura")
    ResponseEntity getCertificateCoverageDocumentByPolicyItemId(@PathVariable Long policyItemId) {
        try {
            List<FileDocumentByRequestDTO> fileDocumentByRequestDTOList = fileDocumentUseCase.getCertificateCoverageDocumentByPolicyItemId(policyItemId);
            return ok(fileDocumentByRequestDTOList);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

    @PostMapping(value = FileDocumentEndpoint.REGISTER_DOCUMENT)
    @ApiOperation(value = "Registra los documentos cargados de la solicitud")
    ResponseEntity registerDocuments(@PathVariable Long requestId, @RequestBody FileDocumentByRequestDTO file) {
        try {
            PolicyFileDocument response = fileDocumentUseCase.registerDocument(requestId, file);
            return ok(response);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }


}
