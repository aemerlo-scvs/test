package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CoveragePolicyItemUseCase;
import com.scfg.core.application.port.in.CoverageProductPlanUseCase;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfProductPlanCoverageDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.ClfSaveCoverageDTO;
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
@RequestMapping(path = CoverageEndPoint.BASE)
@Api(tags = "API REST Coberturas")
public class CoverageController implements CoverageEndPoint {

    private final CoverageProductPlanUseCase coverageUseCase;
    private final CoveragePolicyItemUseCase coveragePolicyItemUseCase;

    @GetMapping()
    @ApiOperation(value = "Retorna una lista de coberturas por el codigo de convenio del plan")
    ResponseEntity getAllCoverageByPlanAgreementCode(@RequestParam Integer planAgreementCode) {
        try {
            List<CoverageDTO> response = coverageUseCase.getAllCoverageByAgreementCodePlan(planAgreementCode);
            return ok(response);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = BASE_PARAM_PRODUCT)
    @ApiOperation(value = "Retorna una lista de planes por el codigo de convenio")
    ResponseEntity getCoveragesByApsCodeAndPlanIdAndPolicyItemId(@RequestParam String apsCode,
                                                                 @RequestParam long planId,
                                                                 @RequestParam long policyItemId) {
        try {
            List<ClfProductPlanCoverageDTO> coverageDTOList = coverageUseCase.getAllProductPlanCoverageByProductApsCodeAndPlanIdAndPolicyItemId(apsCode, planId, policyItemId);
            return ok(coverageDTOList);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(CoverageEndPoint.BASE_PARAM_COVERAGE_POLICY_ITEM)
    @ApiOperation(value = "Retorna una lista de planes por el código de convenio")
    ResponseEntity saveCoverage(@RequestBody ClfSaveCoverageDTO oSaveCoverage) {
        try {
            Boolean response = coveragePolicyItemUseCase.saveOrUpdateAll(oSaveCoverage);
            return ok(response);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
