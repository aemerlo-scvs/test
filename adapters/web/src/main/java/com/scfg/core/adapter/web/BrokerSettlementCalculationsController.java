package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.BrokerSettlementCalculationsUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = BrokerSettlementCalculationsEndpoint.BROKER_SETTLEMENT_CALCULATIONS_BASE_ROUTE)
@ApiOperation(value = "APIs Calculos de liquidacion del broker")
public class BrokerSettlementCalculationsController implements BrokerSettlementCalculationsEndpoint {

    private final BrokerSettlementCalculationsUseCase brokerSettlementCalculationsUseCase;

    // Falta agregar la relacion con la tabla de Item desgravamen

    @PostMapping(value = MANAGER_CALCULATIONS_BY_POLICY_TYPE)
    @Override
    public ResponseEntity registerBrokerCalculations(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long reportTypeId,
            @RequestParam long usersId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> brokerSettlementCalculations) {


        ObjectMapper objectMapper = HelpersMethods.mapper();

        /*ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();*/

        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)
            List<BrokerSettlementCalculationsDhlDTO> calculationsForDHL = objectMapper.convertValue(brokerSettlementCalculations, new TypeReference<List<BrokerSettlementCalculationsDhlDTO>>() {
            });

            //new BrokerSettlementCalculationsDhnDTO()

            return ok(brokerSettlementCalculationsUseCase.saveCalculationsForRegulatedPolicy(policyTypeId,
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    reportTypeId,
                    usersId,
                    calculationsForDHL,
                    overwrite
            ));
        } else { // NOT REGULATED POLICY
            List<BrokerSettlementCalculationsDhnDTO> calculationForDHN = objectMapper.convertValue(brokerSettlementCalculations, new TypeReference<List<BrokerSettlementCalculationsDhnDTO>>() {
            });

            return ok(brokerSettlementCalculationsUseCase.saveCalculationsForNotRegulatedPolicy(
                    policyTypeId,
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    reportTypeId,
                    usersId,
                    calculationForDHN,
                    overwrite
            ));
        }
    }

    @GetMapping(value = MANAGER_CALCULATIONS_BY_POLICY_TYPE,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity getBrokerSettlementCalculationsFiltered(
            @PathVariable long policyTypeReferenceId,
            //@RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId) {
        try {
            if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                    .getReferenceCode()) {
                //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)

                List<BrokerSettlementCalculationsDhlDTO> calculationsForDHL = brokerSettlementCalculationsUseCase.getBrokerSettlementCalculationsDHLFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(calculationsForDHL);
            } else { // NOT REGULATED POLICY
                List<BrokerSettlementCalculationsDhnDTO> calculationsForDHN = brokerSettlementCalculationsUseCase.getBrokerSettlementCalculationsDHNFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(calculationsForDHN);
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }
}
