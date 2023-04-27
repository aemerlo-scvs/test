package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.GenerateReportsUseCase;
import com.scfg.core.application.port.in.PreliminaryObservedCaseUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = PreliminaryObservedCaseEndpoint.OBSERVED_CASES_BASE_ROUTE)
@ApiOperation(value = "APIs Casos Observados Preliminares")
public class PreliminaryObservedCaseController implements PreliminaryObservedCaseEndpoint {

    private final PreliminaryObservedCaseUseCase preliminaryObservedCaseUseCase;
    private final GenerateReportsUseCase generateReportsUseCase;

    @GetMapping(value = MANAGER_PRELIMINARY_OBSERVED_CASES_BY_POLICY_TYPE, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recupera el listado de casos observados generados por el sistema")
    @Override
    public ResponseEntity getPreliminaryObservedCases(@PathVariable long policyTypeReferenceId,
                                                      @RequestParam long monthId,
                                                      @RequestParam long yearId,
                                                      @RequestParam long insurancePolicyHolderId,
                                                      HttpServletRequest request) {
        try {
            List<Object> listForGenerateExcel;
            List<PreliminaryObservedCaseDTO> preliminaryObservedCaseDTOList;
            List<String> columnsForGenerateExcel = preliminaryObservedCaseUseCase.getPreliminaryObservedCasesColumns();

            if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode()) {
                preliminaryObservedCaseDTOList = preliminaryObservedCaseUseCase
                        .getPreliminaryObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);
                //return ResponseEntity.ok(preliminaryObservedCaseDTOList);
            } else {
                preliminaryObservedCaseDTOList = preliminaryObservedCaseUseCase
                        .getPreliminaryObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);
            }

            if(preliminaryObservedCaseDTOList.isEmpty()){
                return CustomErrorType.notContent(PRELIMINARY_OBSERVED_CASE_TITLE, MESSAGE_NOT_FOUND_PRELIMINARY_OBSERVED_CASES);
            }
            listForGenerateExcel = Collections.singletonList(preliminaryObservedCaseDTOList);

            FileDocumentDTO file = generateReportsUseCase.generateExcel(
                    FIRST_COLUMN_FILE_PRELIMINARY_OBSERVED_CASE,
                    LAST_COLUMN_FILE_PRELIMINARY_OBSERVED_CASE,
                    columnsForGenerateExcel,
                    listForGenerateExcel,
                    FILENAME_PRELIMINARY_OBSERVED_CASE);

            return ok(file);

        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }
}
