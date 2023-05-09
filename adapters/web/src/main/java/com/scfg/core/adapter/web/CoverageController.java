package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.CoveragePolicyItemUseCase;
import com.scfg.core.application.port.in.CoverageProductPlanUseCase;
import com.scfg.core.application.port.in.CoverageUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Clause;
import com.scfg.core.domain.Coverage;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
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

    private final CoverageProductPlanUseCase coverageProductPlanUseCase;
    private final CoveragePolicyItemUseCase coveragePolicyItemUseCase;
    private final CoverageUseCase coverageUseCase;
    @GetMapping(value =  CoverageEndPoint.GETALL)
    @ApiOperation(value = "Listado de las coberturas")
    public ResponseEntity findAll() {
        List<Coverage> branchList = coverageUseCase.getAllCoverage();
        if (branchList.isEmpty()) {
            return CustomErrorType.notContent("Get branchs", "No data");
        }
        return ok(branchList);
    }

    @PostMapping(value = CoverageEndPoint.SAVE)
    @ApiOperation(value = "Guardar las coberturas")
    public ResponseEntity guardar(@RequestBody Coverage coverage) {
        try {
            PersistenceResponse response = coverageUseCase.registerCoverage(coverage);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Coverage", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }

    }
    @PostMapping(value = CoverageEndPoint.UPDATE)
    @ApiOperation(value = "Actualizar las coberturas")
    public ResponseEntity update(@RequestBody Coverage coverage) {
        try {
            PersistenceResponse response = coverageUseCase.updateCoverage(coverage);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Coverage", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @DeleteMapping(value = CoverageEndPoint.DELETE)
    @ApiOperation(value = "Dar de baja la coberturas")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            PersistenceResponse response = coverageUseCase.deleteCoverage(id);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Coverage", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @PostMapping(value = CoverageEndPoint.FILTER)
    @ApiOperation(value = "Lista de Coberturas por filtro")
    public ResponseEntity getAllBranchParents(@RequestBody FilterParamenter paramenter) {
        try {
            List<Coverage> branchList = coverageUseCase.getfilterParamenter(paramenter);
            return ok(branchList);
        }catch (Exception ex){
            return CustomErrorType.notContent("Get branchs",ex.getMessage());
        }

    }
//    @GetMapping(value = CoverageEndPoint.FINDBYBRANCHID)
//    @ApiOperation(value = "Lista de cobeturas por código de ramo")
//    public ResponseEntity getAllBranchParents(@PathVariable Long branchId) {
//        try {
//            List<Coverage> branchList = coverageUseCase.findByBranchId(branchId);
//            return ok(branchList);
//        }catch (Exception ex){
//            return CustomErrorType.notContent("Get branchs",ex.getMessage());
//        }
//
//    }
    @GetMapping(value = "findCoverageByProductId/{productId}")
    @ApiOperation(value = "Listado de coberturas por productos id")
    public ResponseEntity getCoverageByProductId(@PathVariable Long productId) {
        try {
            List<Coverage> coverageList = coverageUseCase.getAllCoverageByProductId(productId);
            return ok(coverageList);
        }catch (Exception ex){
            return CustomErrorType.notContent("coberturas por producto id",ex.getMessage());
        }

    }
    @GetMapping()
    @ApiOperation(value = "Retorna una lista de coberturas por el codigo de convenio del plan")
    ResponseEntity getAllCoverageByPlanAgreementCode(@RequestParam Integer planAgreementCode) {
        try {
            List<CoverageDTO> response = coverageProductPlanUseCase.getAllCoverageByAgreementCodePlan(planAgreementCode);
            return ok(response);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = "/product")
    @ApiOperation(value = "Retorna una lista de planes por el codigo de convenio")
    ResponseEntity getCoveragesByApsCodeAndPlanIdAndPolicyItemId(@RequestParam String apsCode,
                                                                 @RequestParam long planId,
                                                                 @RequestParam long policyItemId) {
        try {
            List<ClfProductPlanCoverageDTO> coverageDTOList = coverageProductPlanUseCase.getAllProductPlanCoverageByProductApsCodeAndPlanIdAndPolicyItemId(apsCode, planId, policyItemId);
            return ok(coverageDTOList);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/coveragePolicyItem")
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
