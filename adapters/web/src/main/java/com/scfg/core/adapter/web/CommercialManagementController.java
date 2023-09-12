package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CommercialManagementUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/commercial-management")
@Api(tags = "API REST de la Gestion Comercial de renovacion de polizas")
public class CommercialManagementController {
    private final CommercialManagementUseCase commercialManagementUseCase;


    @PostMapping(value = "/save")
    @ApiOperation(value = "Registrar La gestion comercial")
    public ResponseEntity<PersistenceResponse> save(@RequestBody CommercialManagement obj) {
        try {
            PersistenceResponse response = commercialManagementUseCase.save(obj);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("CommercialManagement", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @PostMapping(value ="/update")
    @ApiOperation(value = "Actualiza la gestion comercial")
    public ResponseEntity<PersistenceResponse> update(@RequestBody CommercialManagement obj) {
        try {
            PersistenceResponse response = commercialManagementUseCase.update(obj);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("CommercialManagement", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @PostMapping(value ="/updateSomeFields")
    @ApiOperation(value = "Actualiza la gestion comercial")
    public ResponseEntity<PersistenceResponse> updateSomeFields(@RequestBody CommercialManagement obj) {
        try {
            PersistenceResponse response = commercialManagementUseCase.updateSomeFields(obj);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("CommercialManagement", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @GetMapping(value = "/getById/{id}")
    @ApiOperation(value = "Obtener detalle")
    public ResponseEntity getById(@PathVariable Long id) {
        try {
            CommercialManagement obj = commercialManagementUseCase.findById(id);
            return ok(obj);
        }catch (Exception ex){
            return CustomErrorType.notContent("Commercial management object",ex.getMessage());
        }

    }
    @PostMapping(value = "/search")
    @ApiOperation(value = "Retorna una lista de polizas a renovar por filtros dinamicos")
    ResponseEntity getAllByFilters(@RequestBody @Nullable CommercialManagementSearchFiltersDTO commercialManagementSearchFiltersDto) {
        try {
            List<CommercialManagementDTO> list = commercialManagementUseCase.search(commercialManagementSearchFiltersDto);
            return ok(list);
        } catch (OperationException e) {
            log.error("Ocurrio un error al obtener la lista: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la lista: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/search-json")
    @ApiOperation(value = "Retorna una lista de polizas a renovar por filtros dinamicos")
    ResponseEntity getAllByFiltersJSON(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer subStatus,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate) {
        try {
            String list = commercialManagementUseCase.searchJSON(status,subStatus,fromDate,toDate);
            return ok(list);
        } catch (OperationException e) {
            log.error("Ocurrio un error al obtener la lista: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener la lista: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
