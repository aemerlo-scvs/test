package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CommercialManagementUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/commercial-management")
@Api(tags = "API REST de la Gestion Comercial de renovacion de polizas")
public class CommercialManagementController {
    private final CommercialManagementUseCase commercialManagementUseCase;


    @PostMapping(value = "/search")
    @ApiOperation(value = "Retorna una lista de polizas a renovar por filtros dinamicos")
    ResponseEntity getAllByFilters(@RequestBody @Nullable CommercialManagementSearchFiltersDTO commercialManagementSearchFiltersDto) {
        try {
            PersistenceResponse response = commercialManagementUseCase.getAllByFilters(commercialManagementSearchFiltersDto);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrio un error al obtener la lista: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la lista: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/all")
    @ApiOperation(value = "Listado de polizas a renovar")
    public ResponseEntity getAll() {
        PersistenceResponse response = commercialManagementUseCase.getAll();
//        if (response.isEmpty()) {
//            return CustomErrorType.notContent("Get Policies to renew", "No data");
//        }
        return ok(response);
    }

    @GetMapping(value = "/getDetail/:policyId")
    @ApiOperation(value = "Informacion de la poliza a renovar")
    public ResponseEntity findByPolicyId(@PathVariable Long policyId) {
        PersistenceResponse response = commercialManagementUseCase.findByPolicyId(policyId);
//        if (data == null) {
//            return CustomErrorType.notContent("Get Policy to renew", "No data");
//        }
        return ok(response);
    }

}
