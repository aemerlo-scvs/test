package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.service.VIRHProcessService;
import com.scfg.core.common.exception.OperationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/virh")
@Api(tags = "API REST VIRH")
public class VIRHController {
    private final   VIRHProcessService service;
    @GetMapping (value = "/informationPolicy")
    @ApiOperation(value = "Servicio para recuperar información (plan, asegurado, beneficiario)")
    ResponseEntity informationPolicy(@Param("param") String param) {
        try {
           String data= this.service.getDataInformationPolicy(param);
            return ok(data);
        } catch (OperationException e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error recuperar la información: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", "No se pudo realizar la operación, " + e.getMessage());
        }
    }
}
