package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.service.VinService;
import com.scfg.core.common.exception.OperationException;
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
@RequestMapping(path = "/processRequest")
@Api(tags = "API Rest - Procesamiento de solicitudes")
public class ProcessRequestController {
    private final VinService vinService;

    @PostMapping
    @ApiOperation(value = "Se valida todo el proceso y se genera los documentos según sean necesarios")
    ResponseEntity processRequest(@RequestBody Object requestDTO, @RequestParam String productInitial) {
        try {
            switch (productInitial) {
                case "VIN":
                    vinService.processRequest(requestDTO);
                    return ok(true);
                default:
                    log.error("Ocurrió un error al querer procesar la solicitud: [{}]", "Error al procesar solicitud de: " + productInitial);
                    return CustomErrorType.badRequest("Error al procesar la solicitud", "No se puede procesar el producto: " + productInitial);
            }
        } catch (OperationException e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
