package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.SinisterUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SinisterDhnDTO;
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
@RequestMapping(path = SinisterEndpoint.SINISTER_BASE_ROUTE)
@ApiOperation(value = "APIs Calculos de liquidacion del broker")
public class SinisterController implements SinisterEndpoint {

    private final SinisterUseCase sinisterUseCase;

    // Falta agregar la relacion con la tabla de Item desgravamen

    @PostMapping(value = MANAGER_SINISTERS_BY_POLICY_TYPE)
    @Override
    public ResponseEntity registerSinisters(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long reportTypeId,
            @RequestParam long usersId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> sinisters) {


        ObjectMapper objectMapper = HelpersMethods.mapper();

        /*ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();*/

        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)
            List<SinisterDhlDTO> sinistersForDHL = objectMapper.convertValue(sinisters, new TypeReference<List<SinisterDhlDTO>>() {
            });

            //new BrokerSettlementCalculationsDhnDTO()

            return ok(sinisterUseCase.saveSinistersForRegulatedPolicy(policyTypeId,
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    reportTypeId,
                    usersId,
                    sinistersForDHL,
                    overwrite
            ));
        } else { // NOT REGULATED POLICY
            List<SinisterDhnDTO> sinistersForDHN = objectMapper.convertValue(sinisters, new TypeReference<List<SinisterDhnDTO>>() {
            });

            return ok(sinisterUseCase.saveSinistersForNotRegulatedPolicy(
                    policyTypeId,
                    monthId,
                    yearId,
                    insurancePolicyHolderId,
                    reportTypeId,
                    usersId,
                    sinistersForDHN,
                    overwrite
            ));
        }
    }

    @GetMapping(value = MANAGER_SINISTERS_BY_POLICY_TYPE,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity getSinistersFiltered(
            @PathVariable long policyTypeReferenceId,
            //@RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId) {
        try {
            if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                    .getReferenceCode()) {
                //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)

                List<SinisterDhlDTO> calculationsForDHL = sinisterUseCase.getSinistersDHLFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(calculationsForDHL);
            } else { // NOT REGULATED POLICY
                List<SinisterDhnDTO> calculationsForDHN = sinisterUseCase.getSinistersDHNFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(calculationsForDHN);
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }
}
