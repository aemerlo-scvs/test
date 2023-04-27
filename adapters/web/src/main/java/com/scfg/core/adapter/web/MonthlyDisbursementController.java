package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.MonthlyDisbursementUseCase;
import com.scfg.core.application.port.in.PastMonthlyDisbursementUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = MonthlyDisbursemenEndpoint.MONTHLY_DISBURSEMENT_BASE_ROUTE)
@ApiOperation(value = "APIs Desembolos bancarios mensuales")
public class MonthlyDisbursementController implements MonthlyDisbursemenEndpoint {

    private final MonthlyDisbursementUseCase monthlyDisbursementUseCase;
    private final PastMonthlyDisbursementUseCase pastMonthlyDisbursementUseCase;

    // Falta agregar la relacion con la tabla de Item desgravamen

    @PostMapping(value = REGISTER_MONTHLY_DISBURSEMENT__BY_POLICY_TYPE)
    @Override
    public ResponseEntity<PersistenceResponse> registerMonthlyDisbursement(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long reportTypeId,
            @RequestParam long usersId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> monthlyDisbursements) {

        ObjectMapper objectMapper = HelpersMethods.mapper();


        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            
            if (overwrite != HelpersConstants.ACCEPT_OVERWRITE_INFORMATION){
                pastMonthlyDisbursementUseCase.migrateInformationToPastMonthlyDisbursementsForRegulatedPolicy(
                        monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, overwrite);
            }

            List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhl = objectMapper.convertValue(monthlyDisbursements, new TypeReference<List<MonthlyDisbursementDhlDTO>>() {
            });
            
            return ok(monthlyDisbursementUseCase.saveMonthlyDisbursementsForRegulatedPolicy(
                    monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, monthlyDisbursementDhl, overwrite));
        } else { // NOT REGULATED POLICY
            
            if (overwrite != HelpersConstants.ACCEPT_OVERWRITE_INFORMATION){
                pastMonthlyDisbursementUseCase.migrateInformationToPastMonthlyDisbursementsForNotRegulatedPolicy(
                        monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, overwrite);
            }
            List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhn = objectMapper.convertValue(monthlyDisbursements, new TypeReference<List<MonthlyDisbursementDhnDTO>>() {
            });

            return ok(monthlyDisbursementUseCase.saveMonthlyDisbursementsForNotRegulatedPolicy(
                    monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, monthlyDisbursementDhn, overwrite));
        }
    }

    @GetMapping(value = REGISTER_MONTHLY_DISBURSEMENT__BY_POLICY_TYPE,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getFiltered(
            @PathVariable long policyTypeReferenceId,
            //@RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId) {
        try {
            if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                    .getReferenceCode()) {
                //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)

                List<MonthlyDisbursementDhlDTO> calculationsForDHL = monthlyDisbursementUseCase.getMonthlyDisbursementDHLFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(calculationsForDHL);
            } else { // NOT REGULATED POLICY
                List<MonthlyDisbursementDhnDTO> calculationsForDHN = monthlyDisbursementUseCase.getMonthlyDisbursementDHNFiltered(monthId, yearId, insurancePolicyHolderId);;

                return ok(calculationsForDHN);
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }

}
