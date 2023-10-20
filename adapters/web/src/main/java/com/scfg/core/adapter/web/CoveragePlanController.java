package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CoveragePlanUseCase;
import com.scfg.core.domain.CoveragePlan;
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
@RequestMapping(path = "/coverage-plan")
@Api(tags = "API REST Coberturas de los planes de un producto")
public class CoveragePlanController {
    private final CoveragePlanUseCase coveragePlanUseCase;

    @GetMapping(value = "/findByPlan/{planId}")
    @ApiOperation(value = "Listado de la relacion de las coberturas de un plan")
    ResponseEntity getAllByProduct(@PathVariable Long planId) {
        try {
            List<CoveragePlan> list = coveragePlanUseCase.getAllCoveragePlanByPlanId(planId);
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

}
