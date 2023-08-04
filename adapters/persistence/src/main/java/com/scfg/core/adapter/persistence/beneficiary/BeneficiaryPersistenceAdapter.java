package com.scfg.core.adapter.persistence.beneficiary;

import com.scfg.core.adapter.persistence.policy.PolicyJpaEntity;
import com.scfg.core.application.port.out.BeneficiaryPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.Policy;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class BeneficiaryPersistenceAdapter implements BeneficiaryPort {
    private final BeneficiaryRepository beneficiaryRepository;


    @Override
    public List<Beneficiary> findAll() {
        Object o = beneficiaryRepository.findAll();

        return (List<Beneficiary>) o;
    }

    @Override
    public Boolean saveOrUpdate(Beneficiary o) throws Exception {
        BeneficiaryJpaEntity beneficiaryJpaEntity = mapToJpaEntity(o);
        try {
            beneficiaryJpaEntity = beneficiaryRepository.save(beneficiaryJpaEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return beneficiaryJpaEntity != null;
    }

    @Override
    public Boolean saveAll(List<Beneficiary> lis) {
        List<BeneficiaryJpaEntity> beneficiaryJpaEntityList = new ArrayList<>();
        beneficiaryJpaEntityList = mapToJpaEntityList(lis);
        try {
            beneficiaryJpaEntityList = beneficiaryRepository.saveAll(beneficiaryJpaEntityList);
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    @Override
    public Boolean saveAllOrUpdateCLF(List<Beneficiary> lis) {
        List<BeneficiaryJpaEntity> beneficiaryJpaEntityList = new ArrayList<>();
        beneficiaryJpaEntityList = mapToJpaEntityListCLF(lis);
        try {
            beneficiaryJpaEntityList = beneficiaryRepository.saveAll(beneficiaryJpaEntityList);
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    @Override
    public List<Beneficiary> findAllByPolicyItemId(Long policyItemId) {
        List<BeneficiaryJpaEntity> beneficiaryJpaEntities = beneficiaryRepository.findAllByPolicyItemId(policyItemId);
        return mapToDomainListCLF(beneficiaryJpaEntities);
    }

    @Override
    public List<Beneficiary> findAllByGeneralRequestId(Long generalRequestId) {
        List<BeneficiaryJpaEntity> beneficiaryJpaEntities = beneficiaryRepository.findAllByGeneralRequestId(
                generalRequestId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return mapToDomainListCLF(beneficiaryJpaEntities);
    }

    private List<BeneficiaryJpaEntity> mapToJpaEntityListCLF(List<Beneficiary> lis) {
        List<BeneficiaryJpaEntity> jpaEntityList = new ArrayList<>();
        lis.forEach(beneficiary -> {
            jpaEntityList.add(mapToJpaEntityCLF(beneficiary));
        });
        return jpaEntityList;
    }

    private List<Beneficiary> mapToDomainListCLF(List<BeneficiaryJpaEntity> lis) {
        List<Beneficiary> jpaEntityList = new ArrayList<>();
        lis.forEach(beneficiary -> {
            jpaEntityList.add(mapToDomainCLF(beneficiary));
        });
        return jpaEntityList;
    }

    private List<BeneficiaryJpaEntity> mapToJpaEntityList(List<Beneficiary> lis) {
        List<BeneficiaryJpaEntity> jpaEntityList = new ArrayList<>();
        lis.forEach(beneficiary -> {
            jpaEntityList.add(mapToJpaEntity(beneficiary));
        });
        return jpaEntityList;
    }

    private BeneficiaryJpaEntity mapToJpaEntity(Beneficiary o) {
        BeneficiaryJpaEntity beneficiaryJpaEntity = BeneficiaryJpaEntity.builder()
                //.id(o.getId() == 0 || o.getId() == null ? null : o.getId())
                .birthDate(o.getBirthDate())
                .name(o.getName())
                .identification(o.getIdentification())
                .lastName(o.getLastName())
                .marriedLastName(o.getMarriedLastName())
                .percentage(o.getPercentage())
                .relationShipIdc(o.getRelationshipIdc())
                .motherLastName(o.getMotherLastName())
                .createdAt(o.getCreatedAt())
                .lastModifiedAt(o.getLastModifiedAt())
                .policy(PolicyJpaEntity.builder().id(o.getPolicyId()).build())
                .policyItemId(o.getPolicyItemId())
                .legalExt(o.getLegalExt())
                .legalIdentification(o.getLegalIdentification())
                .representativeLegalName(o.getRepresentativeLegalName())
                .isUnderAge(o.getIsUnderAge() == 2 ? true : false)
                .telephone(o.getTelephone())
                .build();
        return beneficiaryJpaEntity;
    }

    private BeneficiaryJpaEntity mapToJpaEntityCLF(Beneficiary o) {
        BeneficiaryJpaEntity beneficiaryJpaEntity = BeneficiaryJpaEntity.builder()
                //.id(o.getId() == 0 || o.getId() == null ? null : o.getId())
                .birthDate(o.getBirthDate())
                .name(o.getName())
                .identification(o.getIdentification())
                .lastName(o.getLastName())
                .marriedLastName(o.getMarriedLastName())
                .percentage(o.getPercentage())
                .relationShipIdc(o.getRelationshipIdc())
                .motherLastName(o.getMotherLastName())
                .createdAt(o.getCreatedAt())
                .lastModifiedAt(o.getLastModifiedAt())
                .policy(PolicyJpaEntity.builder().id(o.getPolicyId()).build())
                .policyItemId(o.getPolicyItemId())
                .legalExt(o.getLegalExt())
                .legalIdentification(o.getLegalIdentification())
                .representativeLegalName(o.getRepresentativeLegalName())
                .isUnderAge(o.getIsUnderAge() == 2 ? true : false)
                .telephone(o.getTelephone())
                .build();
        return beneficiaryJpaEntity;
    }

    private Beneficiary mapToDomainCLF(BeneficiaryJpaEntity o) {
        Beneficiary beneficiary = Beneficiary.builder()
                .id(o.getId())
                .birthDate(o.getBirthDate())
                .name(o.getName())
                .identification(o.getIdentification())
                .lastName(o.getLastName())
                .marriedLastName(o.getMarriedLastName())
                .percentage(o.getPercentage())
                .relationshipIdc(o.getRelationShipIdc())
                .motherLastName(o.getMotherLastName())
                .createdAt(o.getCreatedAt())
                .lastModifiedAt(o.getLastModifiedAt())
                .policyId(o.getPolicy().getId())
                .policyItemId(o.getPolicyItemId())
                .legalExt(o.getLegalExt())
                .legalIdentification(o.getLegalIdentification())
                .representativeLegalName(o.getRepresentativeLegalName())
                .isUnderAge(o.isUnderAge() ? 2 : 1)
                .telephone(o.getTelephone())
                .build();
        return beneficiary;
    }
}
