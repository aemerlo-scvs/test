package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.PolicyUseCase;
import com.scfg.core.application.port.in.ProposalUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.RequestProposalSearchFiltersDto;
import com.scfg.core.domain.dto.credicasas.groupthefont.GELPolicyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/proposal")
@Api(tags = "API REST Propuesta")
public class ProposalController implements PlanEndPoint{
    private final ProposalUseCase proposalUseCase;

    @PostMapping(value = "/search-proposal/{planId}")
    @ApiOperation(value = "Retorna una lista de propuestas")
    ResponseEntity getAllByIdPlan(@RequestParam Integer page, @RequestParam Integer size,
                                  @PathVariable Long planId,
                                  @RequestBody RequestProposalSearchFiltersDto filtersDto) {
        try {
            PageableDTO response = proposalUseCase.getAllByIdPlanPageAndProposalFilters(page, size, planId, filtersDto);
            return ok(response);
        } catch (OperationException e) {
            log.error("Ocurrió un error al obtener la lista de propuestas: [{}]", e.toString());
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrió un error al obtener la lista de propuestas: [{}]", e.toString());
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
