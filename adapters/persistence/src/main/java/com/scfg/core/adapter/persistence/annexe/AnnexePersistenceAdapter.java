package com.scfg.core.adapter.persistence.annexe;

import com.scfg.core.application.port.out.AnnexePort;
import com.scfg.core.common.PersistenceAdapter;

import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.dto.RequestAnnexeCancelaltionDto;
import com.scfg.core.domain.dto.vin.AnnexeDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexePersistenceAdapter implements AnnexePort {
    private final AnnexeRepository annexeRepository;

    @Override
    public Long saveOrUpdate(RequestAnnexeCancelaltionDto annexe, Long requestAnnexeId) {
        Long annexeNumber = this.annexeRepository.getNumberAnnexe(annexe.getPolicyId());
        AnnexeJpaEntity annexeJpaEntity = AnnexeJpaEntity.builder()
                .annexeNumber(annexeNumber != null ? annexeNumber.intValue()+1 : 1)
                .policy(annexe.getPolicyId())
                .annexeTypeIdc(annexe.getAnnexeTypeId().intValue())
                .requestAnnexe(requestAnnexeId)
                .startDate(LocalDateTime.now())
                .endDate(DateUtils.asDateToLocalDateTime(annexe.getEndDate()))
                .issuanceDate(LocalDateTime.now())
                .build();
        this.annexeRepository.save(annexeJpaEntity);
        return annexeJpaEntity.getId();
    }
    @Override
    public List<AnnexeDTO> getAllAnnexeByAnnexeTypeAndRequestAnnexeId(Long annexeTypeId, Long requestAnnexeId) {
        List<AnnexeJpaEntity> annexeJpaEntityList = this.annexeRepository.getOptionalByAnnexeTypeIdAndRequestAnnexeId(
                        annexeTypeId.intValue(),
                        requestAnnexeId,
                        PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<AnnexeDTO> annexeDTOList = new ArrayList<>();
        annexeJpaEntityList.forEach(annexe -> {
            annexeDTOList.add(new ModelMapper().map(annexe, AnnexeDTO.class));
        });
        return annexeDTOList;
    }

    @Override
    public AnnexeDTO findAnnexeByIdOrThrowExcepcion(Long annexeId) {
        AnnexeJpaEntity annexeJpaEntity = annexeRepository.findOptionalById(annexeId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue())
                .orElseThrow(() -> new NotDataFoundException("Anexo no encontrado"));
        return new ModelMapper().map(annexeJpaEntity, AnnexeDTO.class);
    }

}
