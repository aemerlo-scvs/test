package com.scfg.core.adapter.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.SubscriptionTrackingUseCase;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionTrackingDhnDTO;
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
@RequestMapping(path = SubscriptionTrackingEndpoint.SUBSCRIPTION_TRACKING_BASE_ROUTE)
@ApiOperation(value = "APIs Seguimiento de suscripcion de asegurados")
public class SubscriptionTrackingController implements SubscriptionTrackingEndpoint {

    private final SubscriptionTrackingUseCase subscriptionTrackingUseCase;

    // Falta agregar la relacion con la tabla de Item desgravamen

    @PostMapping(value = REGISTER_SUBSCRIPTION_TRACKING_BY_POLICY_TYPE)
    @Override
    public ResponseEntity<PersistenceResponse> registerSubscriptionTracking(
            @PathVariable long policyTypeReferenceId,
            @RequestParam long policyTypeId,
            @RequestParam long monthId,
            @RequestParam long yearId,
            @RequestParam long insurancePolicyHolderId,
            @RequestParam long reportTypeId,
            @RequestParam long usersId,
            @RequestParam(defaultValue = "0") long overwrite,
            @RequestBody List<Object> subscriptionsTracking) {

       /* ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();*/
        ObjectMapper objectMapper = HelpersMethods.mapper();

        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()) {
            //List<BrokerSettlementCalculationsDhlDTO> dhl= castObjectsToDTO (brokerSettlementCalculations)

            List<SubscriptionTrackingDhlDTO> subscriptionTrackingDhlDTOS = objectMapper.convertValue(subscriptionsTracking, new TypeReference<List<SubscriptionTrackingDhlDTO>>() {
            });

            return ok(subscriptionTrackingUseCase.saveSubscriptionsTrackingForRegulatedPolicy(monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, subscriptionTrackingDhlDTOS, overwrite));
        } else { // NOT REGULATED POLICY
            List<SubscriptionTrackingDhnDTO> subscriptionTrackingDhnDTOS = objectMapper.convertValue(subscriptionsTracking, new TypeReference<List<SubscriptionTrackingDhnDTO>>() {
            });

            return ok(subscriptionTrackingUseCase.saveSubscriptionsTrackingForNotRegulatedPolicy(monthId, yearId, insurancePolicyHolderId, reportTypeId, policyTypeId, usersId, subscriptionTrackingDhnDTOS, overwrite));
        }
    }

    @GetMapping(value = REGISTER_SUBSCRIPTION_TRACKING_BY_POLICY_TYPE,
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

                List<SubscriptionTrackingDhlDTO> subscriptionTrackingDhlDTOS = subscriptionTrackingUseCase.getSubscriptionTrackingDHLFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(subscriptionTrackingDhlDTOS);
            } else { // NOT REGULATED POLICY
                List<SubscriptionTrackingDhnDTO> subscriptionTrackingDhnDTOS = subscriptionTrackingUseCase.getSubscriptionTrackingDHNFiltered(monthId, yearId, insurancePolicyHolderId);

                return ok(subscriptionTrackingDhnDTOS);
            }
        } catch (Exception e) {
            return CustomErrorType.serverError("server error", e.getMessage());
        }
    }


}
