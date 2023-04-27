package com.scfg.core.adapter.persistence.TempCajeros;

import com.scfg.core.application.port.out.TempCajerosPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.smvs.TempCajerosDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class TempCajerosPersistenceAdapter implements TempCajerosPort {
    private final TempCajerosRepository tempCajerosRepository;
    @Override
    public PersistenceResponse save(List<TempCajerosDto> tempCajerosDto) {
        tempCajerosRepository.deleteAll();
        List<TempCajerosJpaEntity> tempCajerosJpaEntityList= new ArrayList<>();
        for (TempCajerosDto v: tempCajerosDto) {
            tempCajerosJpaEntityList.add(mapDomianToEntity(v));
        }
        tempCajerosJpaEntityList=tempCajerosRepository.saveAll(tempCajerosJpaEntityList);
        return new PersistenceResponse(
                TempCajerosPersistenceAdapter.class.getName(), ActionRequestEnum.CREATE,tempCajerosJpaEntityList.size()
        );

    }

    @Override
    public String getAsignacionCajerosPorCadaMes() {
        String result=tempCajerosRepository.getExcuteAsignacionCajerosPorCadaMes();
        return  result;
    }

    private TempCajerosJpaEntity mapDomianToEntity(TempCajerosDto v) {
        TempCajerosJpaEntity tempCajerosJpaEntity= TempCajerosJpaEntity.builder()
                .cajeros(v.getCajeros())
                .branch(v.getBranch())
                .codAgency(v.getCodAgency())
                .dateNF(v.getDateNF())
                .codClient(v.getCodClient())
                .descriptionPosition(v.getDescriptionPosition())
                .names(v.getNames())
                .namesAgency(v.getNamesAgency())
                .objectives(v.getObjectives())
                .typePaf(v.getTypePaf())
                .zones(v.getZones())
                .rural(v.getRural())
                .codBranch(v.getCodBranch())
                .dateCt(v.getDateCt())
                .statusLoad(v.getStatusLoad())
                .status(v.getStatus())
                .createdAt(v.getCreatedAt())
                .lastModifiedAt(v.getLastModifiedAt())
//                .createdBy(v.getCreatedBy())
//                .lastModifiedBy(v.getLastModifiedBy())
                .build();
        return tempCajerosJpaEntity;
    }
}
