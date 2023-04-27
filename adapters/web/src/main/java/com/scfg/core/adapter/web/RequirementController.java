package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.RequirementControlUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.dto.credicasas.RegisterRequirementControlDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlDTO;
import com.scfg.core.domain.dto.credicasas.RequirementControlGetDTO;
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
@RequestMapping(path = RequirementEndPoint.BASE)
@Api(tags = "API RequestDetail")
public class RequirementController {
    private final RequirementControlUseCase requirementControlUseCase;

    @GetMapping(value = RequirementEndPoint.REQUEST_ID)
    @ApiOperation(value = "Retorna el detalle de solicitud por Id")
    ResponseEntity getRequirementsByRequestId(@PathVariable Long requestId) {
        try {
            List<RequirementControlGetDTO> requirementControlDTOList = requirementControlUseCase.getAllRequirementsById(requestId);
            return ok(requirementControlDTOList);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

    @PostMapping()
    @ApiOperation(value = "Añade un nuevo requerimiento")
    ResponseEntity addRequirement(@RequestBody RegisterRequirementControlDTO registerRequirementControlDTO) {
        try {
            Long id = requirementControlUseCase.addRequirement(registerRequirementControlDTO);
            return ok(id);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

    @PutMapping()
    @ApiOperation(value = "Edita un requerimiento")
    ResponseEntity editRequirement(@RequestBody RegisterRequirementControlDTO registerRequirementControlDTO) {
        try {
            Long id = requirementControlUseCase.editRequirement(registerRequirementControlDTO);
            return ok(id);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

    @PutMapping(value = RequirementEndPoint.DELETE)
    @ApiOperation(value = "Elimina un requerimiento")
    ResponseEntity deleteRequirement(@RequestBody RegisterRequirementControlDTO registerRequirementControlDTO) {
        try {
            Long id = requirementControlUseCase.deleteRequirement(registerRequirementControlDTO);
            return ok(id);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

}
