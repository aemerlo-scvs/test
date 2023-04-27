package com.scfg.core.adapter.persistence.juridicalPerson;

import com.scfg.core.adapter.persistence.document.DocumentJpaEntity;
import com.scfg.core.adapter.persistence.policy.PolicyJpaEntity;
import com.scfg.core.application.port.out.JuridicalPersonPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.enums.TypesDocumentPersonEnum;
import com.scfg.core.domain.Policy;
import com.scfg.core.domain.dto.JuridicalPersonDTO;
import com.scfg.core.domain.dto.JuridicalPersonWithPersonIdDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class JuridicalPersonPersistenceAdapter implements JuridicalPersonPort {

    private final JuridicalPersonRepository juridicalPersonRepository;

    @Override
    public List<JuridicalPersonDTO> getAllJuridicalPerson(int assignedGroup) {
        List<JuridicalPersonDTO> juridicalPersonDTOS = new ArrayList<>();
        List<JuridicalPersonWithPersonIdDTO> juridicalPerson = juridicalPersonRepository.findAllJuridicalPersonByAssignedGroup(assignedGroup, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        juridicalPerson.forEach(e -> {
            List<Policy> policyList = new ArrayList<>();
            List<PolicyJpaEntity> policyJpaEntityList = juridicalPersonRepository.findAllPolicyJP(e.getPersonId(), PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
            policyJpaEntityList.forEach(pol -> {
                policyList.add(mapToDomainPolicy(pol));
            });
            DocumentJpaEntity doc = juridicalPersonRepository.getByJuridicPerson(e.getPersonId(), TypesDocumentPersonEnum.LOGOPJ.getValue(),PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
            juridicalPersonDTOS.add(mapToEntity(e,policyList,doc));
        });
        return juridicalPersonDTOS;
    }

    private Policy mapToDomainPolicy(PolicyJpaEntity policyJpaEntity) {
        Policy policy = Policy.builder()
                .id(policyJpaEntity.getId())
                .generalRequestId(policyJpaEntity.getGeneralRequestId())
                .currencyTypeIdc(policyJpaEntity.getCurrencyTypeIdc())
                .correlativeNumber(policyJpaEntity.getCorrelativeNumber())
                .numberPolicy(policyJpaEntity.getNumberPolicy())
                .issuanceDate(policyJpaEntity.getIssuanceDate())
                .fromDate(policyJpaEntity.getFromDate())
                .toDate(policyJpaEntity.getToDate())
                .totalPremium(policyJpaEntity.getTotalPremium())
                .additionalPremium(policyJpaEntity.getAdditionalPremium())
                .netPremium(policyJpaEntity.getNetPremium())
                .riskPremium(policyJpaEntity.getRiskPremium())
                .aps(policyJpaEntity.getAps())
                .fpa(policyJpaEntity.getFpa())
                .iva(policyJpaEntity.getIva())
                .it(policyJpaEntity.getIt())
                .intermediaryCommission(policyJpaEntity.getIntermediaryCommission())
                .intermediaryCommissionPercentage(policyJpaEntity.getIntermediaryCommissionPercentage())
                .collectionServiceCommission(policyJpaEntity.getCollectionServiceCommission())
                .collectionServiceCommissionPercentage(policyJpaEntity.getCollectionServiceCommissionPercentage())
                .insuredCapital(policyJpaEntity.getInsuredCapital())
                .policyStatusIdc(policyJpaEntity.getPolicyStatusIdc())
                .branchOfficeId(policyJpaEntity.getBranchOfficeId())
                .agencyId(policyJpaEntity.getAgencyId())
                .renewal(policyJpaEntity.getRenewal())
                .assignedExecutiveId(policyJpaEntity.getAssignedExecutiveId())
                .intermediaryTypeIdc(policyJpaEntity.getIntermediaryTypeIdc())
                .brokerId(policyJpaEntity.getBrokerId())
                .agentId(policyJpaEntity.getAgentId())
                .createdAt(policyJpaEntity.getCreatedAt())
                .lastModifiedAt(policyJpaEntity.getLastModifiedAt())
                .build();
        return policy;
    }

    private JuridicalPersonDTO mapToEntity(JuridicalPersonWithPersonIdDTO person, List<Policy> policyList, DocumentJpaEntity doc) {
        return JuridicalPersonDTO.builder()
                .name(person.getName())
                .businessTypeIdc(person.getBusinessTypeIdc())
                .imageLogo(doc.getContent())
                .mimeType(doc.getMimeType())
                .policyDto(policyList)
                .build();
    }

}
