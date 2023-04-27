package com.scfg.core.adapter.persistence.requestDetail;

import com.scfg.core.application.port.out.RequestDetailPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import lombok.RequiredArgsConstructor;


@PersistenceAdapter
@RequiredArgsConstructor
public class RequestDetailPersistenceAdapter implements RequestDetailPort {
    private final RequestDetailRepository requestDetailRepository;

    @Override
    public RequestDetailDTO getRequestDetailById(Long requestId) {
        RequestDetailDTO requestDetailDTO = requestDetailRepository.findRequestDetailById(
                requestId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        return requestDetailDTO;
    }

    @Override
    public String getJuridicalNameByPolicyItemId(Long policyItemId) {
        return requestDetailRepository.findJuridicalNameByPolicyItemId(policyItemId);
    }

}
