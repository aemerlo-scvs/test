package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.JksCertificateUseCase;
import com.scfg.core.application.service.JksCertificateService;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.JksCertificateDTO;
import com.scfg.core.domain.dto.SignDocumentDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/jksCertificate")
@Api(tags = "Certificado Jks")
public class JksCertificateController {

    private final JksCertificateService jksCertificateService;
    private final JksCertificateUseCase jksCertificateUseCase;

    @GetMapping
    ResponseEntity getAll() {
        try {
            List<JksCertificateDTO> response = jksCertificateUseCase.getAll();
            return ok(response);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operacion, " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    ResponseEntity getById(@PathVariable Long id) {
        try {
            JksCertificateDTO response = jksCertificateUseCase.getById(id);
            return ok(response);
        } catch (OperationException e) {
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operacion, " + e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity save(@RequestBody JksCertificateDTO jksCertificateDTO) {
        try {
            Boolean response = jksCertificateUseCase.saveOrUpdate(jksCertificateDTO);
            return ok(response);
        } catch (OperationException e) {
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operacion, " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualiza un cliente PEP")
    ResponseEntity update(@RequestBody JksCertificateDTO jksCertificateDTO) {
        try {
            Boolean response = jksCertificateUseCase.saveOrUpdate(jksCertificateDTO);
            return ok(response);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/sign")
    ResponseEntity signWithP12Cert(@RequestBody SignDocumentDTO signDocumentDTO) {
        try {
            String response = jksCertificateUseCase.signDocumentWithP12Cert(signDocumentDTO.getBase64Document(),
                    signDocumentDTO.getOwnerSigns(), LocalDateTime.now());
            return ok(response);
        } catch (OperationException e) {
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operacion, " + e.getMessage());
        }
    }

}
