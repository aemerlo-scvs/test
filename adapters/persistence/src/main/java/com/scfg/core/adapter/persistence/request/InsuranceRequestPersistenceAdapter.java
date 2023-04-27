package com.scfg.core.adapter.persistence.request;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierPersistenceAdapter;
import com.scfg.core.application.port.out.InsuranceRequestPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.InsuranceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.scfg.core.common.util.HelpersConstants.*;
import static com.scfg.core.common.util.HelpersMethods.*;


@PersistenceAdapter
@RequiredArgsConstructor
public class InsuranceRequestPersistenceAdapter implements InsuranceRequestPort {

    private final InsuranceRequestRepository insuranceRequestRepository;

    @Override
    public List<InsuranceRequest> getAllInsuranceRequests() {
        Object insuranceRequestList = insuranceRequestRepository.findAll();
        return (List<InsuranceRequest>) insuranceRequestList;
    }

    @Override
    public InsuranceRequest getInsuranceRequestById(long insuranceRequestId) {
        InsuranceRequestJpaEntity insuranceRequest = insuranceRequestRepository.findById(insuranceRequestId)
                .orElseThrow(() ->
                        new NotDataFoundException("Insurance Request: " + insuranceRequestId + " Not found")
                );
        return mapToDomain(insuranceRequest, false);
    }

    @Override
    public InsuranceRequest getInsuranceRequestByNumber(String insuranceRequestNumber) {
        Page<InsuranceRequestJpaEntity> pages = insuranceRequestRepository.findByRequestNumber(
                insuranceRequestNumber,
                PageRequest.of(0, 1));
        Optional<InsuranceRequestJpaEntity> insuranceRequestJpaEntityOptional = pages.stream().findFirst();

        /*InsuranceRequestJpaEntity insuranceRequest = insuranceRequestRepository.findByRequestNumber(insuranceRequestNumber)
                .orElseThrow(() ->
                        new NotDataFoundException("Insurance Request Number: " + insuranceRequestNumber + " Not found")
                );*/

        return insuranceRequestJpaEntityOptional.isPresent()
                ? mapToDomain(insuranceRequestJpaEntityOptional.get(), false)
                : null;
    }

    @Override
    public PersistenceResponse save(InsuranceRequest insuranceRequest) {
        InsuranceRequestJpaEntity insuranceRequestJpaEntity = mapToJpaEntity(insuranceRequest);
        try {
            insuranceRequestJpaEntity = insuranceRequestRepository.save(insuranceRequestJpaEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return new PersistenceResponse(
                InsuranceRequest.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                insuranceRequestJpaEntity
        );
    }

    @Override
    public PersistenceResponse update(InsuranceRequest insuranceRequest) {
        InsuranceRequestJpaEntity insuranceRequestJpaEntity = mapToJpaEntity(insuranceRequest);
        insuranceRequestJpaEntity = insuranceRequestRepository.save(insuranceRequestJpaEntity);
        return new PersistenceResponse(
                InsuranceRequest.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                insuranceRequestJpaEntity
        );
    }

    @Override
    public PersistenceResponse delete(InsuranceRequest insuranceRequest) {
        InsuranceRequestJpaEntity insuranceRequestJpaEntity = mapToJpaEntity(insuranceRequest);
        // status for deleted
        insuranceRequestJpaEntity.setStatus(PersistenceStatusEnum
                .DELETED
                .getValue());
        insuranceRequestRepository.save(insuranceRequestJpaEntity);

        return new PersistenceResponse(
                InsuranceRequest.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Map<String, Object> findOrUpsert(InsuranceRequest insuranceRequestDomain) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        Page<InsuranceRequestJpaEntity> pages = insuranceRequestRepository.findByRequestNumber(
                insuranceRequestDomain.getRequestNumber(),
                PageRequest.of(0, 1));
        Optional<InsuranceRequestJpaEntity> insuranceRequestJpaEntityOptional = pages.stream().findFirst();
        InsuranceRequestJpaEntity insuranceRequestJpaEntity = insuranceRequestJpaEntityOptional.isPresent()
                ? insuranceRequestJpaEntityOptional.get()
                : null;
        if (isNull(insuranceRequestJpaEntity)) {
            insuranceRequestJpaEntity = new InsuranceRequestJpaEntity();
            result.put(KEY_ACTION_ENTITY, CREATE_ACTION);
        } else {
            result.put(KEY_ACTION_ENTITY, UPDATE_ACTION);
            // validate status of insurance request
            long referenceCodeSrcEntity = insuranceRequestJpaEntity.getRequestStatus()
                    .getReferenceId();
            ;
            long referenceTypeCodeSrcEntity = insuranceRequestJpaEntity.getRequestStatus()
                    .getClassifierType()
                    .getReferenceId();
            long referenceCodeSrcDomain = insuranceRequestDomain.getRequestStatus()
                    .getReferenceId();
            ;
            long referenceTypeCodeSrcDomain = insuranceRequestDomain.getRequestStatus()
                    .getClassifierType()
                    .getReferenceId();

            if (!equalsClassifiers(referenceCodeSrcEntity, referenceTypeCodeSrcEntity,
                    ClassifierEnum.Registered_InsuranceRequestStatus)
                    && equalsClassifiers(referenceCodeSrcDomain, referenceTypeCodeSrcDomain,
                    ClassifierEnum.Registered_InsuranceRequestStatus)) {
                // assing other status different of "REGISTERED"
                insuranceRequestDomain.setRequestStatus(null);
            } else {
                insuranceRequestJpaEntity.refrehsRelationship();
            }

        }

        // assign RequestStatusId for default
        //insuranceRequestJpaEntity = insuranceRequestRepository.saveAndFlush(insuranceRequestJpaEntity);
        mergeValues(insuranceRequestJpaEntity, insuranceRequestDomain);
        //insuranceRequestJpaEntity.getRequestStatus().setStatus(1);
        insuranceRequestJpaEntity = insuranceRequestRepository.save(insuranceRequestJpaEntity);
        result.put(KEY_CONTENT_ENTITY, mapToDomain(insuranceRequestJpaEntity, true));

        return result;
        /*return mapToDomain(insuranceRequestRepository.findByRequestNumber(insuranceRequest.getRequestNumber())
                .orElseGet(() -> insuranceRequestRepository.saveAndFlush(mapToJpaEntity(insuranceRequest))));
        return mapToDomain(insuranceRequestRepository.saveAndFlush(mapToJpaEntity(insuranceRequest)));*/
    }

    /***
     * Persist model in database
     * @param insuranceRequest
     * @return
     */
    public static InsuranceRequestJpaEntity mapToJpaEntity(InsuranceRequest insuranceRequest) {
        return InsuranceRequestJpaEntity.builder()
                .id(insuranceRequest.getId())
                .requestNumber(insuranceRequest.getRequestNumber())
                .insuranceRequestDate(insuranceRequest.getInsuranceRequestDate())
                .schedulingDate(insuranceRequest.getSchedulingDate())
                .fulfillmentRequirementsDate(insuranceRequest.getFulfillmentRequirementsDate())
                .requestedAmount(insuranceRequest.getRequestedAmount())
                .accumulatedAmount(insuranceRequest.getAccumulatedAmount())
                .disbursementDate(insuranceRequest.getDisbursementDate())
                .exclusionDate(insuranceRequest.getExclusionDate())
                .inclusionDate(insuranceRequest.getInclusionDate())
                .djsFillDate(insuranceRequest.getDjsFillDate())
                .acceptanceDate(insuranceRequest.getAcceptanceDate())
                .pronouncementDate(insuranceRequest.getPronouncementDate())
                .bankPronouncementDate(insuranceRequest.getBankPronouncementDate())
                .djsReceptionDate(insuranceRequest.getDjsReceptionDate())
                // relationship
                .requestStatus(ClassifierJpaEntity.builder()
                        .id(insuranceRequest.getRequestStatusId())
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(insuranceRequest.getCurrencyId())
                        .build())
                // base entity
                .createdAt(insuranceRequest.getCreatedAt())
                .lastModifiedAt(insuranceRequest.getLastModifiedAt())
                .build();

        /*InsuranceRequestJpaEntity resultJPA = new InsuranceRequestJpaEntity();
        resultJPA.setId(insuranceRequest.getId());
        resultJPA.setRequestNumber(insuranceRequest.getRequestNumber());
        resultJPA.setSchedulingDate(insuranceRequest.getSchedulingDate());
        resultJPA.setDjsReceptionDate(insuranceRequest.getDjsReceptionDate());
        resultJPA.setPronouncementDate(insuranceRequest.getPronouncementDate());
        resultJPA.setFulfillmentRequirementsDate(insuranceRequest.getFulfillmentRequirementsDate());
        resultJPA.setInclusionDate(insuranceRequest.getInclusionDate());
        resultJPA.setExclusionDate(insuranceRequest.getExclusionDate());
        resultJPA.setDjsFillDate(insuranceRequest.getDjsFillDate());
        resultJPA.setDisbursementDate(insuranceRequest.getDisbursementDate());
        resultJPA.setCreatedAt(insuranceRequest.getCreatedAt());
        resultJPA.setLastModifiedAt(insuranceRequest.getLastModifiedAt());

        // for relations
        resultJPA.setRequestStatus(ClassifierJpaEntity.builder()
                .id(insuranceRequest.getRequestStatusId())
                .build());
        resultJPA.setCurrency(ClassifierJpaEntity.builder()
                .id(insuranceRequest.getCurrencyId())
                .build());
        return resultJPA;*/
    }

    /***
     * Export domain for consume APIs
     * @param insuranceRequestJpaEntity
     * @return
     */
    public static InsuranceRequest mapToDomain(
            InsuranceRequestJpaEntity insuranceRequestJpaEntity,
            boolean withRelation) {
        return InsuranceRequest.builder()
                .id(insuranceRequestJpaEntity.getId())
                .requestNumber(insuranceRequestJpaEntity.getRequestNumber())
                .insuranceRequestDate(insuranceRequestJpaEntity.getInsuranceRequestDate())
                .schedulingDate(insuranceRequestJpaEntity.getSchedulingDate())
                .fulfillmentRequirementsDate(insuranceRequestJpaEntity.getFulfillmentRequirementsDate())
                .requestedAmount(insuranceRequestJpaEntity.getRequestedAmount())
                .accumulatedAmount(insuranceRequestJpaEntity.getAccumulatedAmount())
                .disbursementDate(insuranceRequestJpaEntity.getDisbursementDate())
                .exclusionDate(insuranceRequestJpaEntity.getExclusionDate())
                .inclusionDate(insuranceRequestJpaEntity.getInclusionDate())
                .djsFillDate(insuranceRequestJpaEntity.getDjsFillDate())
                .acceptanceDate(insuranceRequestJpaEntity.getAcceptanceDate())
                .pronouncementDate(insuranceRequestJpaEntity.getPronouncementDate())
                .bankPronouncementDate(insuranceRequestJpaEntity.getBankPronouncementDate())
                .djsReceptionDate(insuranceRequestJpaEntity.getDjsReceptionDate())
                // relationship
                .requestStatusId(withRelation
                        ? insuranceRequestJpaEntity.getRequestStatus().getId()
                        : null)
                .currencyId(withRelation
                        ? insuranceRequestJpaEntity.getCurrency().getId()
                        : null)
                .requestStatus(ClassifierPersistenceAdapter.mapToDomain(insuranceRequestJpaEntity.getRequestStatus()))
                .currency(ClassifierPersistenceAdapter.mapToDomain(insuranceRequestJpaEntity.getCurrency()))

                // base entity
                .createdAt(insuranceRequestJpaEntity.getCreatedAt())
                .lastModifiedAt(insuranceRequestJpaEntity.getLastModifiedAt())
                .build();
        /*InsuranceRequest result = new InsuranceRequest();
        result.setId(insuranceRequestJpaEntity.getId());
        result.setRequestNumber(insuranceRequestJpaEntity.getRequestNumber());
        result.setSchedulingDate(insuranceRequestJpaEntity.getSchedulingDate());
        result.setDjsReceptionDate(insuranceRequestJpaEntity.getDjsReceptionDate());
        result.setPronouncementDate(insuranceRequestJpaEntity.getPronouncementDate());
        result.setFulfillmentRequirementsDate(insuranceRequestJpaEntity.getFulfillmentRequirementsDate());
        result.setInclusionDate(insuranceRequestJpaEntity.getInclusionDate());
        result.setExclusionDate(insuranceRequestJpaEntity.getExclusionDate());
        result.setDjsFillDate(insuranceRequestJpaEntity.getDjsFillDate());
        result.setDisbursementDate(insuranceRequestJpaEntity.getDisbursementDate());
        result.setCreatedAt(insuranceRequestJpaEntity.getCreatedAt());
        result.setLastModifiedAt(insuranceRequestJpaEntity.getLastModifiedAt());
        result.setRequestStatusId(insuranceRequestJpaEntity.getRequestStatus().getId());
        result.setCurrencyId(insuranceRequestJpaEntity.getCurrency().getId());

        return result;*/
    }
}
