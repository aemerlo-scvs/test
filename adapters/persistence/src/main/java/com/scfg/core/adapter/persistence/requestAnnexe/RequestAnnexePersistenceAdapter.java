package com.scfg.core.adapter.persistence.requestAnnexe;

import com.scfg.core.application.port.out.RequestAnnexePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.enums.RequestStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.common.AnnexeRequirement;
import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class RequestAnnexePersistenceAdapter implements RequestAnnexePort {

    private final RequestAnnexeRepository requestAnnexeRepository;
    @Override
    public Long saveOrUpdate(RequestAnnexeDTO o) {
        RequestAnnexeJpaEntity requestAnnexeJpaEntity = mapToJpaEntity(o);
        requestAnnexeJpaEntity = requestAnnexeRepository.save(requestAnnexeJpaEntity);
        return requestAnnexeJpaEntity.getId();
    }

    @Override
    public List<RequestAnnexeDTO> getRequestByPolicyIdAndAnnexeTypeIdAndStatus(Long policyId, Long annexeTypeId,
                                                                               Integer requestStatus) {
        List<RequestAnnexeJpaEntity> list = requestAnnexeRepository.findAllByPolicyIdAndAnnexeTypeIdAndStatusIdc(policyId,
                annexeTypeId,requestStatus);
        return list.stream().map(o -> new ModelMapperConfig().getStrictModelMapper()
                .map(o, RequestAnnexeDTO.class)).collect(Collectors.toList());
    }

    //#region Mappers
    private RequestAnnexeJpaEntity mapToJpaEntity(RequestAnnexeDTO requestAnnexeDTO) {
        RequestAnnexeJpaEntity requestAnnexeJpaEntity= new ModelMapperConfig().getStrictModelMapper()
                .map(requestAnnexeDTO, RequestAnnexeJpaEntity.class);
        requestAnnexeJpaEntity.setPolicyId(requestAnnexeDTO.getPolicyDetail().getId());
        requestAnnexeJpaEntity.setStatusIdc(RequestStatusEnum.PENDING.getValue());
        return requestAnnexeJpaEntity;
    }
    //#endregion
}
