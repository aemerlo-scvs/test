package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.AnnexeRequirementControlUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
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
@RequestMapping(path = "annexeRequirementControl")
@Api(tags = "API REST Solicitud de anexo")
public class AnnexeRequirementControlController {
    private final AnnexeRequirementControlUseCase annexeRequirementControlUseCase;

    @GetMapping(value = "/{requestAnnexeId}/{annexeTypeId}")
    @ApiOperation(value = "Retorna la lista de requerimientos por el Id de solicitud y el tipo de anexo")
    ResponseEntity getAllByAnnexeId(@PathVariable Long requestAnnexeId, @PathVariable Long annexeTypeId) {
        try {
            List<AnnexeRequirementDto> list = annexeRequirementControlUseCase.getAllByRequestAnnexeIdAndAnnexeTypeId(requestAnnexeId, annexeTypeId);
            return ok(list);
        } catch (OperationException e) {
            log.error("Ocurrio un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.badRequest("Process Error", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al querer procesar la solicitud: [{}]", e.getMessage());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
