package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.OldMonthlyDisbursementCreditLineUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.OldMonthlyDisbursementCreditLineDhnDTO;
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
@RequestMapping(path = OldMonthlyDisbursementCreditLineEndpoint.OLD_MONTHLY_DISBURSEMENT_BASE_ROUTE)
@ApiOperation(value = "APIs Desembolos bancarios mensuales (pasadas gestiones)")
public class OldMonthlyDisbursementCreditLineController implements OldMonthlyDisbursementCreditLineEndpoint {

    private final OldMonthlyDisbursementCreditLineUseCase oldMonthlyDisbursementCreditLineUseCase;

    // Falta agregar la relacion con la tabla de Item desgravamen

    @PostMapping(value = REGISTER_OLD_MONTHLY_DISBURSEMENT__BY_POLICY_TYPE)
    @Override
    public ResponseEntity<PersistenceResponse> registerOldMonthlyDisbursement(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long reportTypeId,
            @RequestParam long usersId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> oldMonthlyDisbursements) {

        /*ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();*/

        ObjectMapper objectMapper = HelpersMethods.mapper();

        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)

            List<OldMonthlyDisbursementCreditLineDhlDTO> monthlyDisbursementDhl = objectMapper.convertValue(oldMonthlyDisbursements, new TypeReference<List<OldMonthlyDisbursementCreditLineDhlDTO>>() {
            });

            return ok(oldMonthlyDisbursementCreditLineUseCase.saveOldMonthlyDisbursementsForRegulatedPolicy(monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, monthlyDisbursementDhl, overwrite));
        } else { // NOT REGULATED POLICY
            List<OldMonthlyDisbursementCreditLineDhnDTO> monthlyDisbursementDhn = objectMapper.convertValue(oldMonthlyDisbursements, new TypeReference<List<OldMonthlyDisbursementCreditLineDhnDTO>>() {
            });

            return ok(oldMonthlyDisbursementCreditLineUseCase.saveOldMonthlyDisbursementsForNotRegulatedPolicy(monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, monthlyDisbursementDhn, overwrite));
        }
    }

    @GetMapping(value = REGISTER_OLD_MONTHLY_DISBURSEMENT__BY_POLICY_TYPE,
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
                return ok("1");
            } else { // NOT REGULATED POLICY
                List<OldMonthlyDisbursementCreditLineDhnDTO> calculationsForDHN = oldMonthlyDisbursementCreditLineUseCase.getOldMonthlyDisbursementDHNFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(calculationsForDHN);
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }
}
