package com.scfg.core.adapter.persistence.mortgageReliefitem;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.insurancePolicyHolder.InsurancePolicyHolderJpaEntity;
import com.scfg.core.adapter.persistence.user.UserJpaEntity;
import com.scfg.core.application.port.out.MortgageReliefItemPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MortgageReliefItemAdapter implements MortgageReliefItemPort {

    private final MortgageReliefItemRepository mortgageReliefItemRepository;


    @Override
    public List<MortgageReliefItem> getMortgageReliefItemsByIDs(long monthId, long yearId, long reportTypeId, long policyTypeId, long insurancePolicyHolderId) {
        List<MortgageReliefItemJpaEntity> mortgageReliefItemsEntities = mortgageReliefItemRepository.
                findByLoadYear_IdAndLoadMonth_IdAndAndReportType_IdAndPolicyType_IdAndInsurancePolicyHolder_Id(
                        yearId, monthId, reportTypeId, policyTypeId, insurancePolicyHolderId);

        List<MortgageReliefItem> mortgageReliefItemsDomain = new ArrayList<>();
        mortgageReliefItemsEntities.stream()
                .forEach(mortgageReliefItemEntity -> {
                    mortgageReliefItemsDomain.add(mapToDomain(mortgageReliefItemEntity));
                });
        return mortgageReliefItemsDomain;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void disableLastInformation(long monthId, long yearId, long reportTypeId, long policyTypeId, long usersId, long insurancePolicyHolderId) {
        try {
            //mortgageReliefItemRepository.disableLastInformation(monthId, yearId, reportTypeId, policyTypeId/*, usersId*/, insurancePolicyHolderId);
            mortgageReliefItemRepository.deleteByPolicyType_IdAndReportType_IdAndInsurancePolicyHolder_IdAndLoadMonth_IdAndLoadYear_Id(
                    policyTypeId, reportTypeId, insurancePolicyHolderId, monthId, yearId);
        } catch (Exception e) {
            String er = e.getMessage();

        }

    }

    @Override
    public void callSpRollbackRelatedEntities(
            long rollbackCreditOperation, long rollbackInsuranceRequest,
            long rollbackClient, long rollbackMortgageReliefItem) {
        mortgageReliefItemRepository.callSpRollbackRelatedEntities(
                rollbackCreditOperation, rollbackInsuranceRequest,
                rollbackClient, rollbackMortgageReliefItem);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public MortgageReliefItem save(MortgageReliefItem mortgageReliefItemDomain) {
        MortgageReliefItemJpaEntity mortgageReliefItemJpaEntity = mapMortgageReliefItemDtoToJpaEntityForCreate(mortgageReliefItemDomain);
        return mapToDomain(mortgageReliefItemRepository.save(mortgageReliefItemJpaEntity));
    }

    @Override
    public void deleteForPeriod(long monthId, long yearId, long insurancePolicyHolderId, long reportTypeId, long policyTypeId) {
        // parameterize for criteria
        mortgageReliefItemRepository.deleteByPolicyType_IdAndReportType_IdAndInsurancePolicyHolder_IdAndLoadMonth_IdAndLoadYear_Id(
                policyTypeId, reportTypeId, insurancePolicyHolderId, monthId, yearId);

    }

    @Override
    public void deleteAllByPolicyTypeAndReportTypeAndInsuranceHolder(long policyTypeId, long reportTypeId, long insurancePolicyHolderId) {
        mortgageReliefItemRepository.deleteByPolicyType_IdAndReportType_IdAndAndInsurancePolicyHolder_Id(policyTypeId, reportTypeId, insurancePolicyHolderId);
    }

    public static MortgageReliefItemJpaEntity mapMortgageReliefItemDtoToJpaEntityForCreate(MortgageReliefItem mortgageReliefItem) {
        return MortgageReliefItemJpaEntity.builder()
                .policyType(ClassifierJpaEntity.builder()
                        .id(mortgageReliefItem.getPolicyTypeIdc())
                        .build())
                .loadMonth(ClassifierJpaEntity.builder()
                        .id(mortgageReliefItem.getMonthIdc())
                        .build())
                .loadYear(ClassifierJpaEntity.builder()
                        .id(mortgageReliefItem.getYearIdc())
                        .build())
                .reportType(ClassifierJpaEntity.builder()
                        .id(mortgageReliefItem.getReportTypeIdc())
                        .build())
                .insurancePolicyHolder(InsurancePolicyHolderJpaEntity.builder()
                        .id(mortgageReliefItem.getInsurancePolicyHolderIdc())
                        .build())
                .user(UserJpaEntity.builder()
                        .id(mortgageReliefItem.getUsersId())
                        .build())
                //.brokerSettlementCalculations(null)

                /*.commisionBankAmount(brokerSettlementCalculationsDhlDTO.getCOM_FASSIL())
                .totalInsureds(brokerSettlementCalculationsDhlDTO.getCANT_ASEGURADOS())
                // For relationship
                .insuredCoverage(ClassifierJpaEntity.builder()
                        .id(brokerSettlementCalculationsDhlDTO.getID_COBERTURA_ASEGURADOS())
                        .build())*/
                .build();
    }

    /***
     * Export domain for consume APIs
     * @param mortgageReliefItemJpaEntity
     * @return
     */
    public static MortgageReliefItem mapToDomain(MortgageReliefItemJpaEntity mortgageReliefItemJpaEntity) {
        return MortgageReliefItem.builder()
                .id(mortgageReliefItemJpaEntity.getId())
                .monthIdc(mortgageReliefItemJpaEntity.getLoadMonth()
                        .getId())
                .yearIdc(mortgageReliefItemJpaEntity.getLoadYear()
                        .getId())
                .reportTypeIdc(mortgageReliefItemJpaEntity.getReportType()
                        .getId())
                .policyTypeIdc(mortgageReliefItemJpaEntity.getPolicyType()
                        .getId())
                .insurancePolicyHolderIdc(mortgageReliefItemJpaEntity.getInsurancePolicyHolder()
                        .getId())
                .usersId(mortgageReliefItemJpaEntity.getUser()
                        .getId())
                // base entity
                .createdAt(mortgageReliefItemJpaEntity.getCreatedAt())
                .lastModifiedAt(mortgageReliefItemJpaEntity.getLastModifiedAt())
                .build();

        /*MortgageReliefItem result = new MortgageReliefItem();
        result.setId(mortgageReliefItemJpaEntity.getId());
        result.setMonthIdc(mortgageReliefItemJpaEntity.getLoadMonth().getId());
        result.setYearIdc(mortgageReliefItemJpaEntity.getLoadYear().getId());
        result.setReportTypeIdc(mortgageReliefItemJpaEntity.getReportType().getId());
        result.setPolicyTypeIdc(mortgageReliefItemJpaEntity.getPolicyType().getId());
        result.setInsurancePolicyHolderIdc(mortgageReliefItemJpaEntity.getInsurancePolicyHolder().getId());
        result.setUsersId(mortgageReliefItemJpaEntity.getUser().getId());
        result.setCreatedAt(mortgageReliefItemJpaEntity.getCreatedAt());
        result.setLastModifiedAt(mortgageReliefItemJpaEntity.getLastModifiedAt());
        return result;*/
    }



}
