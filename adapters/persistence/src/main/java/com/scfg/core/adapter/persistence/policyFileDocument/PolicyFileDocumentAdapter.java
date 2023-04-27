package com.scfg.core.adapter.persistence.policyFileDocument;

import com.scfg.core.application.port.out.PolicyFileDocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.PolicyFileDocument;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PolicyFileDocumentAdapter implements PolicyFileDocumentPort {
    private final PolicyFileDocumentRepository policyFileDocumentRepository;

    @Override
    public List<PolicyFileDocument> findAll() {
        return null;
    }

    @Override
    public PolicyFileDocument saveOrUpdate(PolicyFileDocument o) {
        PolicyFileDocumentJpaEntity policyFileDocumentJpaEntity = mapToEntity(o);
        policyFileDocumentJpaEntity = policyFileDocumentRepository.save(policyFileDocumentJpaEntity);
        return policyFileDocumentJpaEntity != null ? mapToDomain(policyFileDocumentJpaEntity): null ;
    }

    @Override
    public Boolean saveAllOrUpdate(List<PolicyFileDocument> o) {
        List<PolicyFileDocumentJpaEntity> policyFileDocumentJpaEntities = mapToJpaEntityListCLF(o);
        policyFileDocumentJpaEntities = policyFileDocumentRepository.saveAll(policyFileDocumentJpaEntities);
        return true;
    }

    private PolicyFileDocumentJpaEntity mapToEntity(PolicyFileDocument o) {
        PolicyFileDocumentJpaEntity policyFileDocumentJpaEntity = PolicyFileDocumentJpaEntity.builder()
                .id(o.getId())
                .policyId(o.getPolicyId())
                .policyItemId(o.getPolicyItemId())
                .fileDocumentId(o.getFileDocumentId())
                .isSigned(o.getIsSigned())
                .uploadDate(o.getUploadDate())
                .createdAt(o.getCreatedAt())
                .lastModifiedAt(o.getLastModifiedAt())
                .build();
        return policyFileDocumentJpaEntity;
    }

    private PolicyFileDocument mapToDomain(PolicyFileDocumentJpaEntity o) {
        PolicyFileDocument policyFileDocument = PolicyFileDocument.builder()
                .id(o.getId())
                .policyId(o.getPolicyId())
                .policyItemId(o.getPolicyItemId())
                .fileDocumentId(o.getFileDocumentId())
                .uploadDate(o.getUploadDate())
                .isSigned(o.getIsSigned())
                .createdAt(o.getCreatedAt())
                .lastModifiedAt(o.getLastModifiedAt())
                .build();
        return policyFileDocument;
    }

    private List<PolicyFileDocumentJpaEntity> mapToJpaEntityListCLF(List<PolicyFileDocument> list) {
        List<PolicyFileDocumentJpaEntity> polList = new ArrayList<>();
        list.forEach(lists -> {
            polList.add(mapToJpaEntityCLF(lists));
        });
        return polList;
    }

    private PolicyFileDocumentJpaEntity mapToJpaEntityCLF(PolicyFileDocument o) {
        PolicyFileDocumentJpaEntity fileDocumentJpaEntity = PolicyFileDocumentJpaEntity.builder()
                .id(o.getId())
                .policyId(o.getPolicyId())
                .policyItemId(o.getPolicyItemId())
                .fileDocumentId(o.getFileDocumentId())
                .uploadDate(o.getUploadDate())
                .createdAt(o.getCreatedAt())
                .isSigned(o.getIsSigned())
                .lastModifiedAt(o.getLastModifiedAt())
                .documentNumber(o.getDocumentNumber())
                .build();
        return fileDocumentJpaEntity;
    }
}
