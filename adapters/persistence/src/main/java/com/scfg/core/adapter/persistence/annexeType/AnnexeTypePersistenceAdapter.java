package com.scfg.core.adapter.persistence.annexeType;

import com.scfg.core.application.port.out.AnnexeTypePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.common.AnnexeType;
import lombok.RequiredArgsConstructor;

import java.util.List;
@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexeTypePersistenceAdapter implements AnnexeTypePort {


    private final AnnexeTypeRepository annexeTypeRepository;


    @Override
    public List<AnnexeType> findAll() {
        Object o = annexeTypeRepository.findAll();
        return (List<AnnexeType>) o;
    }
}
