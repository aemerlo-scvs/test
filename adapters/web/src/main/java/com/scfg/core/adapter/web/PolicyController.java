package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.PlanUseCase;
import com.scfg.core.application.port.in.PolicyUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.Policy;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.GELPolicyDTO;
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
@RequestMapping(path = "/policy")
@Api(tags = "API REST Polizas")
public class PolicyController implements PlanEndPoint{
    private final PolicyUseCase policyUseCase;

    //#Todo Verificar si el metodo de la región "Deprecated" debe ser deprecado o no.
    //#region Deprecated
    @GetMapping(value = "/gel/policies")
    @ApiOperation(value = "Retorna una lista de pólizas pertenecientes al grupo empresarial Lafuente")
    ResponseEntity getAllGELPolicies() {
        try {
            List<GELPolicyDTO> list = policyUseCase.getAllGELPolicies();
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    //#endregion

    @GetMapping(value = "/gel/policies-group")
    @ApiOperation(value = "Retorna una lista de pólizas pertenecientes al grupo empresarial Lafuente")
    ResponseEntity getAllActualGELPolicies(@RequestParam Long businessGroupIdc) {
        try {
            List<GELPolicyDTO> list = policyUseCase.getAllActualGELPolicies(businessGroupIdc);
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/person-filters")
    @ApiOperation(value = "Retorna una lista de pólizas pertenecientes al grupo empresarial Lafuente")
    ResponseEntity getAllByPageAndPersonFilters(@RequestParam Integer page, @RequestParam Integer size, @RequestBody PersonDTO personDTO) {
        try {
            PageableDTO response = policyUseCase.getAllByPageAndPersonFilters(page, size, personDTO);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrió un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al obtener la lista de solicitudes: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
