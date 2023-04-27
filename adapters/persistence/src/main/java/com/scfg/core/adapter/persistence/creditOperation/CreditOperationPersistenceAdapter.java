package com.scfg.core.adapter.persistence.creditOperation;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierPersistenceAdapter;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestPersistenceAdapter;
import com.scfg.core.application.port.out.CreditOperationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.morgtageReliefLiquidation.CreditOperationNotFoundException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.CreditOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.scfg.core.common.util.HelpersMethods.*;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreditOperationPersistenceAdapter implements CreditOperationPort {

    private final CreditOperationRepository creditOperationRepository;

    @Override
    public List<CreditOperation> getAllCreditOperations() {
        Object creditOperationList = creditOperationRepository.findAll();
        return (List<CreditOperation>) creditOperationList;
    }

    @Override
    public CreditOperation getCreditOperationById(long creditOperationId) {
        CreditOperationJpaEntity creditOperation = creditOperationRepository.findById(creditOperationId)
                .orElseThrow(() ->
                        new NotDataFoundException("Credit Operation: " + creditOperationId + " Not found")
                );
        return mapToDomain(creditOperation, false);
    }

    @Override
    public CreditOperation getCreditOperationByNumber(long creditOperationNumber) {
        Page<CreditOperationJpaEntity> pages = creditOperationRepository.findByOperationNumber(
                creditOperationNumber,
                PageRequest.of(0, 1));
        Optional<CreditOperationJpaEntity> creditOperationJpaEntityOptional = pages.stream()
                .findFirst();

        if (creditOperationJpaEntityOptional.isPresent()){
            return mapToDomain(creditOperationJpaEntityOptional.get(), false);
        }
        throw new CreditOperationNotFoundException("Operacion Crediticia Nro. "+ creditOperationNumber + " no fue encontrada");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public PersistenceResponse save(CreditOperation creditOperation, boolean returnEntity) {
        CreditOperationJpaEntity creditOperationJpaEntity = mapToJpaEntity(creditOperation);
        try {
            creditOperationJpaEntity = creditOperationRepository.save(creditOperationJpaEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return new PersistenceResponse(
                CreditOperation.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                returnEntity ? creditOperationJpaEntity : null
        );
    }

    @Override
    public PersistenceResponse update(CreditOperation creditOperation) {
        CreditOperationJpaEntity creditOperationJpaEntity = mapToJpaEntity(creditOperation);
        creditOperationJpaEntity = creditOperationRepository.save(creditOperationJpaEntity);
        return new PersistenceResponse(
                CreditOperation.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                creditOperationJpaEntity
        );
    }

    @Override
    public PersistenceResponse delete(CreditOperation creditOperation) {
        CreditOperationJpaEntity creditOperationJpaEntity = mapToJpaEntity(creditOperation);
        // status for deleted
        creditOperationJpaEntity.setStatus(PersistenceStatusEnum
                .DELETED
                .getValue());
        creditOperationRepository.save(creditOperationJpaEntity);

        return new PersistenceResponse(
                CreditOperation.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Map<String, Object> findOrUpsert(CreditOperation creditOperationDomain) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> result = new HashMap<>();

        Page<CreditOperationJpaEntity> pages = creditOperationRepository.findByOperationNumber(
                creditOperationDomain.getOperationNumber(),
                PageRequest.of(0, 1));
        Optional<CreditOperationJpaEntity> creditOperationJpaEntityOptional = pages.stream().findFirst();
        CreditOperationJpaEntity creditOperationJpaEntity = creditOperationJpaEntityOptional.isPresent()
                ? creditOperationJpaEntityOptional.get()
                : null;
        if (isNull(creditOperationJpaEntity)) {
            creditOperationJpaEntity = new CreditOperationJpaEntity();
            result.put(KEY_ACTION_ENTITY, CREATE_ACTION);
        } else {
            result.put(KEY_ACTION_ENTITY, UPDATE_ACTION);
        }


        // validate change of Insurance Request
        creditOperationJpaEntity.refrehsRelationship();
        //creditOperationJpaEntity = creditOperationRepository.saveAndFlush(creditOperationJpaEntity);
        mergeValues(creditOperationJpaEntity, creditOperationDomain);
        creditOperationJpaEntity = creditOperationRepository.save(creditOperationJpaEntity);

        result.put(KEY_CONTENT_ENTITY, mapToDomain(creditOperationJpaEntity, false));
        return result;


        /*try {

        } catch (Exception e){
            String err = e.getMessage();
        }
        return null;*/
        /*CreditOperationJpaEntity creditOperationJpaEntity = creditOperationRepository.findByOperationNumber(creditOperation.getOperationNumber())
                .orElseGet(() -> creditOperationRepository.saveAndFlush(mapToJpaEntity(creditOperation)));

        return mapToDomain(creditOperationRepository.saveAndFlush(mapToJpaEntity(creditOperation)));*/
    }

    /***
     * Persist model in database
     * @param creditOperation
     * @return
     */
    public static CreditOperationJpaEntity mapToJpaEntity(CreditOperation creditOperation) {
        return CreditOperationJpaEntity.builder()
                .id(creditOperation.getId())
                .operationNumber(creditOperation.getOperationNumber())
                .disbursedAmount(creditOperation.getDisbursedAmount())
                .creditLine(creditOperation.getCreditLine())
                .insuredAmount(creditOperation.getInsuredAmount())
                .disbursementDate(creditOperation.getDisbursementDate())
                .expirationDate(creditOperation.getExpirationDate())
                .deadlineDays(creditOperation.getDeadlineDays())
                .extraPremiumRate(creditOperation.getExtraPremiumRate())
                .extraPremiumValue(creditOperation.getExtraPremiumValue())
                .premiumRate(creditOperation.getPremiumRate())
                .premiumValue(creditOperation.getPremiumValue())
                // relationship
                /*.currency(ClassifierJpaEntity.builder()
                        .id(creditOperation.getCurrencyId())
                        .build())
                .insuranceRequest(InsuranceRequestJpaEntity.builder()
                        .id(creditOperation.getInsuranceRequestId())
                        .build())*/
                .currency(ClassifierPersistenceAdapter.mapToJpaEntity(creditOperation.getCurrency()))
                .insuranceRequest(InsuranceRequestPersistenceAdapter.mapToJpaEntity(creditOperation.getInsuranceRequest()))

                .build();


        /*CreditOperationJpaEntity resultJPA = new CreditOperationJpaEntity();
        resultJPA.setId(creditOperation.getId());
        resultJPA.setCreditLine(creditOperation.getCreditLine());
        resultJPA.setDeadlineDays(creditOperation.getDeadlineDays());
        resultJPA.setExpirationDate(creditOperation.getExpirationDate());
        resultJPA.setExtraPremiumRate(creditOperation.getExtraPremiumRate());
        resultJPA.setExtraPremiumValue(creditOperation.getExtraPremiumValue());
        resultJPA.setInsuredAmount(creditOperation.getInsuredAmount());
        resultJPA.setOperationNumber(creditOperation.getOperationNumber());
        resultJPA.setPremiumRate(creditOperation.getPremiumRate());
        resultJPA.setPremiumValue(creditOperation.getPremiumValue());
        resultJPA.setCreatedAt(creditOperation.getCreatedAt());
        resultJPA.setLastModifiedAt(creditOperation.getLastModifiedAt());

        // for relations
        resultJPA.setCurrency(ClassifierJpaEntity.builder()
                .id(creditOperation.getCurrencyId())
                .build());
        resultJPA.setInsuranceRequest(InsuranceRequestJpaEntity.builder()
                .id(creditOperation.getInsuranceRequestId())
                //.requestNumber("2121212")
                .build());

        return resultJPA;*/
    }

    /***
     * Export domain for consume APIs
     * @param creditOperationJpaEntity
     * @return
     */
    public static CreditOperation mapToDomain(
            CreditOperationJpaEntity creditOperationJpaEntity,
            boolean withRelations) {
        return CreditOperation.builder()
                .id(creditOperationJpaEntity.getId())
                .operationNumber(creditOperationJpaEntity.getOperationNumber())
                .disbursedAmount(creditOperationJpaEntity.getDisbursedAmount())
                .creditLine(creditOperationJpaEntity.getCreditLine())
                .insuredAmount(creditOperationJpaEntity.getInsuredAmount())
                .disbursementDate(creditOperationJpaEntity.getDisbursementDate())
                .expirationDate(creditOperationJpaEntity.getExpirationDate())
                .deadlineDays(creditOperationJpaEntity.getDeadlineDays())
                .extraPremiumRate(creditOperationJpaEntity.getExtraPremiumRate())
                .extraPremiumValue(creditOperationJpaEntity.getExtraPremiumValue())
                .premiumRate(creditOperationJpaEntity.getPremiumRate())
                .premiumValue(creditOperationJpaEntity.getPremiumValue())
                // relationship
                .currencyId(withRelations
                        ? creditOperationJpaEntity.getCurrency().getId()
                        : null)
                .insuranceRequestId(withRelations
                        ? creditOperationJpaEntity.getInsuranceRequest().getId()
                        : null)
                // base class
                .createdAt(creditOperationJpaEntity.getCreatedAt())
                .lastModifiedAt(creditOperationJpaEntity.getLastModifiedAt())
                .build();


        /*CreditOperation result = new CreditOperation();

        result.setId(creditOperationJpaEntity.getId());
        result.setDisbursedAmount(creditOperationJpaEntity.getCreditLine());
        result.setCreditLine(creditOperationJpaEntity.getCreditLine());
        result.setDeadlineDays(creditOperationJpaEntity.getDeadlineDays());
        result.setExpirationDate(creditOperationJpaEntity.getExpirationDate());
        result.setExtraPremiumRate(creditOperationJpaEntity.getExtraPremiumRate());
        result.setExtraPremiumValue(creditOperationJpaEntity.getExtraPremiumValue());
        result.setInsuredAmount(creditOperationJpaEntity.getInsuredAmount());
        result.setOperationNumber(creditOperationJpaEntity.getOperationNumber());
        result.setPremiumRate(creditOperationJpaEntity.getPremiumRate());
        result.setPremiumValue(creditOperationJpaEntity.getPremiumValue());
        result.setCreatedAt(creditOperationJpaEntity.getCreatedAt());
        result.setLastModifiedAt(creditOperationJpaEntity.getLastModifiedAt());
        // relationship
        result.setRequestId(creditOperationJpaEntity.getInsuranceRequest().getId());
        result.setCurrencyId(creditOperationJpaEntity.getCurrency().getId());
        return result;*/
    }
}
