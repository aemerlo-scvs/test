package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.GenerateReportsUseCase;
import com.scfg.core.application.port.in.PreliminaryObservedCaseUseCase;
import com.scfg.core.application.port.in.ValidateInsuredMortgageReliefUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.mortgageRelief.ValidateInsuredsResponse;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.InsuredSummaryDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;
import static com.scfg.core.common.util.HelpersConstants.FILENAME_PRELIMINARY_OBSERVED_CASE;
import static com.scfg.core.common.util.HelpersConstants.*;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = ValidateInsuredMortgageReliefEndpoint.VALIDATE_INSURED_MORTGAGE_RELIEF_BASE_ROUTE)
@ApiOperation(value = "APIs Validacion de asegurados desgravamen hipotecario")
public class ValidateInsuredMortgageReliefController implements ValidateInsuredMortgageReliefEndpoint {

    private final PreliminaryObservedCaseUseCase preliminaryObservedCaseUseCase;
    private final GenerateReportsUseCase generateReportsUseCase;
    private final ValidateInsuredMortgageReliefUseCase validateInsuredMortgageReliefUseCase;

    @PostMapping(value = VALIDATE_INSURED_MORTGAGE_RELIEF_BY_POLICY_TYPE,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity validateInsureds(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long usersId) {

        ValidateInsuredsResponse validateInsuredsResponse;
        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            validateInsuredsResponse = validateInsuredMortgageReliefUseCase.validateInsuredsForRegulatedPolicy(
                    policyTypeReferenceId, policyTypeId, monthId, yearId, insurancePolicyHolderId, usersId);
        } else {
            validateInsuredsResponse = validateInsuredMortgageReliefUseCase.validateInsuredsForUnregulatedPolicy(
                    policyTypeReferenceId, policyTypeId, monthId, yearId, insurancePolicyHolderId, usersId);
        }
        return ok(validateInsuredsResponse);
    }

    @GetMapping(value =GET_INSUREDS_SUMMARY_BY_POLICY_TYPE )
    @Override
    public ResponseEntity getInsuredsSummary(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId) {

        List<InsuredSummaryDTO> insuredSummaryDTOS = validateInsuredMortgageReliefUseCase.getInsuredsSummary(policyTypeReferenceId, policyTypeId, monthId, yearId, insurancePolicyHolderId);
        return ok(insuredSummaryDTOS);

    }

    @GetMapping(value = VALIDATE_INSURED_MORTGAGE_RELIEF_BY_POLICY_TYPE, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recupera el listado de asegurados en regla generados por el sistema")
    @Override
    public ResponseEntity getInsuredsInOrder(@PathVariable long policyTypeReferenceId,
                                             @RequestParam long monthId,
                                             @RequestParam long yearId,
                                             @RequestParam long insurancePolicyHolderId,
                                             HttpServletRequest request) {
        try {
            List<Object> listForGenerateExcel;
            List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlList;
            List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhnList;

            if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode()) {
                monthlyDisbursementDhlList = validateInsuredMortgageReliefUseCase
                        .getInsuredInOrderDHLFiltered(monthId, yearId, insurancePolicyHolderId);
                List<String> columnsForGenerateExcel = validateInsuredMortgageReliefUseCase.getInsuredInOrderDHLColumns();
                if (monthlyDisbursementDhlList.isEmpty()) {
                    return CustomErrorType.notContent(MESSAGE_NOT_FOUND_PRELIMINARY_OBSERVED_CASES, MESSAGE_NOT_FOUND_PRELIMINARY_OBSERVED_CASES);
                }
                listForGenerateExcel = Collections.singletonList(monthlyDisbursementDhlList);

                FileDocumentDTO file = generateReportsUseCase.generateExcel(
                        FIRST_COLUMN_FILE_INSURED_IN_ORDER_DHL,
                        LAST_COLUMN_FILE_INSURED_IN_ORDER_DHL,
                        columnsForGenerateExcel,
                        listForGenerateExcel,
                        FILENAME_INSURED_IN_ORDER_DHL);

                return ok(file);
            } else {
                monthlyDisbursementDhnList = validateInsuredMortgageReliefUseCase
                        .getInsuredInOrderDHNFiltered(monthId, yearId, insurancePolicyHolderId);
                List<String> columnsForGenerateExcel = validateInsuredMortgageReliefUseCase.getInsuredInOrderDHNColumns();
                if (monthlyDisbursementDhnList.isEmpty()) {
                    return CustomErrorType.notContent(MESSAGE_NOT_FOUND_PRELIMINARY_OBSERVED_CASES, MESSAGE_NOT_FOUND_PRELIMINARY_OBSERVED_CASES);
                }
                listForGenerateExcel = Collections.singletonList(monthlyDisbursementDhnList);

                FileDocumentDTO file = generateReportsUseCase.generateExcel(
                        FIRST_COLUMN_FILE_INSURED_IN_ORDER_DHN,
                        LAST_COLUMN_FILE_INSURED_IN_ORDER_DHN,
                        columnsForGenerateExcel,
                        listForGenerateExcel,
                        FILENAME_INSURED_IN_ORDER_DHN);

                return ok(file);
            }

        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }
}
