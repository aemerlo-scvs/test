package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ManualCertificateUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.*;
import io.swagger.annotations.Api;
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
@RequestMapping(path = ManualCertificateEndpoint.MANUAL_CERTIFICATE_BASE_ROUTE)
@Api(value = "API Certificados manuales")
public class ManualCertificateController implements ManualCertificateEndpoint {

    private final ManualCertificateUseCase manualCertificateUseCase;

    @PostMapping(value = REGISTER_MANUAL_CERTIFICATE_BY_POLICY_TYPE)
    @ApiOperation(value = "Registra masivamente certificados manuales")
    @Override
    public ResponseEntity<PersistenceResponse> registerManualCertificates(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long reportTypeId,
            @RequestParam long usersId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> manualCertificates) {


      /*  ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();*/

        ObjectMapper objectMapper = HelpersMethods.mapper();

        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (lastObservedCases)

            List<ManualCertificateDhlDTO> dhl = objectMapper.convertValue(manualCertificates, new TypeReference<List<ManualCertificateDhlDTO>>() {
            });

            return ok(manualCertificateUseCase.registerManualCertificatesForRegulatedPolicy(policyTypeId,
                    monthId, yearId, insurancePolicyHolderId, reportTypeId, usersId, dhl, overwrite));
        } else { // NOT REGULATED POLICY
            List<ManualCertificateDhnDTO> dhn = objectMapper.convertValue(manualCertificates, new TypeReference<List<ManualCertificateDhnDTO>>() {

            });

            return ok(manualCertificateUseCase.registerManualCertificatesForNotRegulatedPolicy(policyTypeId,
                    monthId, yearId, insurancePolicyHolderId, reportTypeId, usersId, dhn, overwrite));
        }
    }

    @GetMapping(value = REGISTER_MANUAL_CERTIFICATE_BY_POLICY_TYPE,
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

                List<ManualCertificateDhlDTO> manualCertificatesDHL = manualCertificateUseCase.getManualCertificateDHLFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(manualCertificatesDHL);
            } else { // NOT REGULATED POLICY
                List<ManualCertificateDhnDTO> manualCertificatesDHN = manualCertificateUseCase.getManualCertificateDHNFiltered(monthId, yearId, insurancePolicyHolderId);


                return ok(manualCertificatesDHN);
            }
        } catch (Exception e) {
            String erro =e.getMessage();
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }
}
