package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CommercialManagementLogUseCase;
import com.scfg.core.application.port.in.CommercialManagementUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.CommercialManagementLog;
import com.scfg.core.domain.Coverage;
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
@RequestMapping(path = "/commercial-management/log")
@Api(tags = "API REST la bitacora de Gestion Comercial renovacion")
public class CommercialManagementLogController {
    private final CommercialManagementLogUseCase cmlUseCase;



    @PostMapping(value = "/save")
    @ApiOperation(value = "Registrar comentarios")
    public ResponseEntity<PersistenceResponse> save(@RequestBody CommercialManagementLog obj) {
        try {
            PersistenceResponse response = cmlUseCase.save(obj);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("CommercialManagementLog", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @GetMapping(value = "/findAllById/{idCommercialManagement}")
    @ApiOperation(value = "Listado de mensajes de la gestion comercial")
    public ResponseEntity findAllByIdCommercialManagement(@PathVariable Long idCommercialManagement) {
        try {
            List<CommercialManagementLog> logList = cmlUseCase.getAllByCommercialManagmentId(idCommercialManagement);
            return ok(logList);
        }catch (Exception ex){
            return CustomErrorType.notContent("Error en obtener los mensajes registrados",ex.getMessage());
        }

    }


}
