package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ConsolidatedObservedCaseUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
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
@RequestMapping(path = ConsolidatedObservedCaseEndpoint.CONSOLIDATED_OBSERVED_CASE_BASE_ROUTE)
@ApiOperation(value = "APIs Casos Observados Consolidados")
public class ConsolidatedObservedCaseController implements ConsolidatedObservedCaseEndpoint {

    private final ConsolidatedObservedCaseUseCase consolidatedObservedCaseUseCase;

    @PostMapping(value = MANAGER_CONSOLIDATED_OBSERVED_CASE_BY_POLICY_TYPE)
    @Override
    public ResponseEntity<PersistenceResponse> registerConsolidatedObservedCase(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long usersId,
            @RequestParam long policyTypeId,
            @RequestParam long reportTypeId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> consolidatedObservedCaseList) {
        /*ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();*/


        ObjectMapper objectMapper = HelpersMethods.mapper();

        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {

            List<ConsolidatedObservedCaseDhlDTO> calculationsForDHL =
                    objectMapper.convertValue(consolidatedObservedCaseList,
                            new TypeReference<List<ConsolidatedObservedCaseDhlDTO>>() {
                            });
            return ok(consolidatedObservedCaseUseCase.saveConsolidatedObservedCaseForRegulatedPolicy(
                    monthId, yearId, insurancePolicyHolderId, usersId, policyTypeId, reportTypeId, calculationsForDHL, overwrite));
        } else {
            // NOT REGULATED POLICY
            List<ConsolidatedObservedCaseDhnDTO> calculationForDHN =
                    objectMapper.convertValue(consolidatedObservedCaseList,
                            new TypeReference<List<ConsolidatedObservedCaseDhnDTO>>() {
                            });
            return ok(consolidatedObservedCaseUseCase.saveConsolidatedObservedCaseForNotRegulatedPolicy(
                    monthId, yearId, insurancePolicyHolderId, usersId, policyTypeId, reportTypeId, calculationForDHN, overwrite));
        }
    }

    @GetMapping(value = MANAGER_CONSOLIDATED_OBSERVED_CASE_BY_POLICY_TYPE,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity getConsolidatedObservedCasesFiltered(@PathVariable long policyTypeReferenceId,
                                                               //@RequestParam long policyTypeId,
                                                               @RequestParam long monthId,
                                                               @RequestParam long yearId,
                                                               @RequestParam long insurancePolicyHolderId) {
        try {
            if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                    .getReferenceCode()) {
                //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)

                //List<Object> consolidatedCasesObservedForDHL = ReportsGenericUseCase.getConsolidatedObservedCaseDHL(); //consolidatedObservedCaseUseCase.getConsolidatedObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);
                List<ConsolidatedObservedCaseDhlDTO> consolidatedCasesObservedForDHL = consolidatedObservedCaseUseCase.getConsolidatedObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(consolidatedCasesObservedForDHL);
            } else { // NOT REGULATED POLICY
                //List<Object> consolidatedCasesObservedForDHN = ReportsGenericUseCase.getConsolidatedObservedCaseDHN(); //consolidatedObservedCaseUseCase.getConsolidatedObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);
                List<ConsolidatedObservedCaseDhnDTO> consolidatedCasesObservedForDHN = consolidatedObservedCaseUseCase.getConsolidatedObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(consolidatedCasesObservedForDHN);
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }
}
