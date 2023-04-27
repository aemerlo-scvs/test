package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ObservedCaseUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.LastObservedCaseDhnDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = ObservedCasesEndpoint.OBSERVED_CASES_BASE_ROUTE)
@ApiOperation(value = "APIs Casos Observados")
public class ObservedCasesController implements ObservedCasesEndpoint {

    private final ObservedCaseUseCase observedCaseUseCase;

    // Falta agregar la relacion con la tabla de Item desgravamen
    @PostMapping(value = MANAGER_LAST_OBSERVED_CASES_BY_POLICY_TYPE)
    @ApiOperation(value = "Registra el listado de casos observados pasados")
    @Override
    public ResponseEntity<PersistenceResponse> registerLastObservedCases(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long reportTypeId,
            @RequestParam long usersId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> lastObservedCases) {

        /*ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();*/

        ObjectMapper objectMapper = HelpersMethods.mapper();

        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (lastObservedCases)

            List<LastObservedCaseDhlDTO> dhl = objectMapper.convertValue(lastObservedCases, new TypeReference<List<LastObservedCaseDhlDTO>>() {
            });

            return ok(observedCaseUseCase.registerLastObservedCasesRegulatedPolicy(policyTypeId,
                    monthId, yearId, insurancePolicyHolderId, reportTypeId, usersId, dhl, overwrite));
        } else { // NOT REGULATED POLICY
            List<LastObservedCaseDhnDTO> dhn = objectMapper.convertValue(lastObservedCases, new TypeReference<List<LastObservedCaseDhnDTO>>() {
            });

            return ok(observedCaseUseCase.registerLastObservedCasesNotRegulatedPolicy(policyTypeId,
                    monthId, yearId, insurancePolicyHolderId, reportTypeId, usersId, dhn, overwrite));
        }
    }

    @GetMapping(value = MANAGER_LAST_OBSERVED_CASES_BY_POLICY_TYPE,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity getLastObservedCasesFiltered(@PathVariable long policyTypeReferenceId,
                                                       //@RequestParam long policyTypeId,
                                                       @RequestParam long monthId,
                                                       @RequestParam long yearId,
                                                       @RequestParam long insurancePolicyHolderId,
                                                       HttpServletRequest request) {
        Object x = request.getAttribute("forceWrite");

        try {
            if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                    .getReferenceCode()) {
                //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)

                List<LastObservedCaseDhlDTO> lastCasesObservedForDHL = observedCaseUseCase.getLastObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(lastCasesObservedForDHL);
            } else { // NOT REGULATED POLICY
                List<LastObservedCaseDhnDTO> lastCasesObservedForDHN = observedCaseUseCase.getLastObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(lastCasesObservedForDHN);
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }


}
