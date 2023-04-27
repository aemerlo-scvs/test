package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.AnnexeRequirementUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.AnnexeRequirement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "annexeRequirement")
@Api(value = "API REST Requisitos del tipo de anexo")
public class AnnexeRequirementController {

    private final AnnexeRequirementUseCase annexeRequirementUseCase;

    @GetMapping(value = "/annexeType/{annexeTypeId}")
    @ApiOperation(value = "Retorna la lista de requerimientos por el Id del tipo de anexo")
    ResponseEntity getById(@PathVariable long annexeTypeId) {
        try{
            List<AnnexeRequirement> requirements = annexeRequirementUseCase.getAllByAnnexeTypeId(annexeTypeId);
            return ok(requirements);
        }catch (OperationException e){
            log.error("Ocurrio un error al obtener los requerimientos del tipo de anexo: [{}]", annexeTypeId, e);
            return  CustomErrorType.badRequest("Bad Request", e.getMessage());
        }catch (Exception e){
            log.error("Ocurrio un error al obtener los requerimientos del tipo de anexo: [{}]", annexeTypeId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
